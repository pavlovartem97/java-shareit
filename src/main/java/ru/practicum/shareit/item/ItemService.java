package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

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
    public ItemDtoOut getItem(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        return itemMapper.map(item);
    }

    @Transactional(readOnly = true)
    public Collection<ItemDtoOut> getAllItemsByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Collection<Item> items = itemRepository.findByUser(user);
        return itemMapper.map(items);
    }

    @Transactional(readOnly = true)
    public Collection<ItemDtoOut> search(String searchText) {
        if (searchText.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepository.searchByNameAndDescription(searchText);
        return itemMapper.map(items);
    }

}
