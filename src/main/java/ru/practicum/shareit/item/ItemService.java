package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingBriefDtoOut;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemBookingBriefInfoDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

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
    public ItemBookingBriefInfoDtoOut getItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        if (item.getUser().getId() != userId) {
            return itemMapper.map(item, null, null);
        }

        return addBookingInformation(item);
    }

    @Transactional(readOnly = true)
    public Collection<ItemBookingBriefInfoDtoOut> getAllItemsByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Collection<Item> items = itemRepository.findByUser(user);
        return items.stream()
                .map(this::addBookingInformation)
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

    ItemBookingBriefInfoDtoOut addBookingInformation(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Optional<BookingRepository.BriefInfoView> lastBriefBookingInfo =
                bookingRepository.findLastBriefBookingInfo(item.getId(), now);
        Optional<BookingRepository.BriefInfoView> nextBriefBookingInfo =
                bookingRepository.findNextBriefBookingInfo(item.getId(), now);

        BookingBriefDtoOut last = lastBriefBookingInfo
                .map(info -> new BookingBriefDtoOut(info.getId(), info.getBookerId()))
                .orElse(null);
        BookingBriefDtoOut next = nextBriefBookingInfo
                .map(info -> new BookingBriefDtoOut(info.getId(), info.getBookerId()))
                .orElse(null);
        return itemMapper.map(item, last, next);
    }
}
