package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemDao {

    Item save(Item item);

    Optional<Item> findByItemId(long itemId);

    Collection<Item> findByUserId(long userId);

    Collection<Item> findByStr(String searchText);

    boolean contains(long itemId);

}
