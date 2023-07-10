package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDao;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserDao userDao;
    private final ItemMapper itemMapper;
    private final ItemDao itemDao;

    public ItemDtoOut addItem(ItemDtoIn itemDto, long userId) {
        User user = userDao.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemMapper.map(itemDto, user);
        itemDao.save(item);
        return itemMapper.map(item);
    }

    public ItemDtoOut updateItem(ItemDtoIn itemDto, long itemId, long userId) {
        User user = userDao.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemDao.findByItemId(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        if (!user.getId().equals(item.getOwner().getId())) {
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
        itemDao.save(item);
        return itemMapper.map(item);
    }

    public ItemDtoOut getItem(long itemId) {
        Item item = itemDao.findByItemId(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        return itemMapper.map(item);
    }

    public Collection<ItemDtoOut> getAllItemsByUserId(long userId) {
        if (!userDao.contains(userId)) {
            throw new NotFoundException("User not found");
        }
        Collection<Item> items = itemDao.findByUserId(userId);
        return itemMapper.map(items);
    }

    public Collection<ItemDtoOut> search(String searchText) {
        if (searchText.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemDao.findBySearchText(searchText);
        return itemMapper.map(items);
    }

}
