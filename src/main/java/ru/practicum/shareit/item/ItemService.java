package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerIsNotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemDao itemDao;

    public ItemDtoOut addItem(ItemDtoIn itemDto, long userId) {
        User user = userService.getUser(userId);
        Item item = itemMapper.map(itemDto, user);
        itemDao.save(item);
        return itemMapper.map(item);
    }

    public ItemDtoOut updateItem(ItemDtoIn itemDto, long itemId, long userId) {
        User user = userService.getUser(userId);
        Item item = itemDao.findByItemId(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item is not found"));
        if (!user.getId().equals(item.getOwner().getId())) {
            throw new OwnerIsNotValidException("User is not item's owner");
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
                .orElseThrow(() -> new ItemNotFoundException("Item is not found"));
        return itemMapper.map(item);
    }

    public Collection<ItemDtoOut> getAllItemsByUserId(long userId) {
        checkUserExist(userId);
        Collection<Item> items = itemDao.findByUserId(userId);
        return itemMapper.map(items);
    }

    public Collection<ItemDtoOut> search(String str) {
        if (str.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemDao.findByStr(str);
        return itemMapper.map(items);
    }

    private void checkUserExist(long userId) {
        userService.getUser(userId);
    }
}
