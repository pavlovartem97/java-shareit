package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingBriefDtoOut;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemExtendedInfoDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ItemDtoOut addItem(ItemDtoIn itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemMapper.map(itemDto, user);
        itemRepository.save(item);
        return itemMapper.map(item);
    }

    @Transactional
    public ItemDtoOut updateItem(ItemDtoIn itemDto, long itemId, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        if (!user.getId().equals(item.getUser().getId())) {
            throw new NotFoundException("User is not item's owner");
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        itemRepository.save(item);
        return itemMapper.map(item);
    }

    @Transactional(readOnly = true)
    public ItemExtendedInfoDtoOut getItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        List<Comment> comments = commentRepository.findByItemOrderByIdDesc(item);
        if (item.getUser().getId() != userId) {
            return itemMapper.map(item, null, null, comments);
        }

        return itemMapper.map(item,
                getLastBookingInfo(item),
                getNextBookingInfo(item),
                comments);
    }

    @Transactional(readOnly = true)
    public Collection<ItemExtendedInfoDtoOut> getAllItemsByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Collection<Item> items = itemRepository.findByUser(user);
        List<Comment> comments = commentRepository.findByItemInOrderById(items);

        Map<Long, List<Comment>> commentMap = new HashMap<>();
        comments.forEach(comment -> {
            Long itemId = comment.getItem().getId();
            if (!commentMap.containsKey(itemId)) {
                commentMap.put(itemId, new ArrayList<>());
            }
            commentMap.get(itemId).add(comment);
        });

        return items.stream()
                .map(item -> itemMapper.map(item,
                        getLastBookingInfo(item),
                        getNextBookingInfo(item),
                        commentMap.getOrDefault(item.getId(), List.of())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public Collection<ItemDtoOut> search(String searchText) {
        if (searchText.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepository.searchByNameAndDescription(searchText);
        return itemMapper.map(items);
    }

    @Transactional
    public CommentDtoOut addComment(CommentDtoIn dto, long userId, long itemId) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        boolean available = bookingRepository.existsBookingByBookerAndItemAndStatusAndStartLessThanEqual(
                user, item, BookingStatus.APPROVED, now);
        if (!available) {
            throw new BadRequestException("Permission denied");
        }

        Comment comment = itemMapper.map(dto, user, item);
        commentRepository.save(comment);
        return itemMapper.map(comment);
    }

    private BookingBriefDtoOut getLastBookingInfo(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Optional<BookingRepository.BriefInfoView> lastBriefBookingInfo =
                bookingRepository.findLastBriefBookingInfo(item.getId(), now);

        return lastBriefBookingInfo
                .map(info -> new BookingBriefDtoOut(info.getId(), info.getBookerId()))
                .orElse(null);
    }

    private BookingBriefDtoOut getNextBookingInfo(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Optional<BookingRepository.BriefInfoView> nextBriefBookingInfo =
                bookingRepository.findNextBriefBookingInfo(item.getId(), now);

        return nextBriefBookingInfo
                .map(info -> new BookingBriefDtoOut(info.getId(), info.getBookerId()))
                .orElse(null);
    }

}
