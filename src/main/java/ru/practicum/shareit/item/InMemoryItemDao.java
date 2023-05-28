package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class InMemoryItemDao implements ItemDao {

    private final Map<Long, Item> items = new TreeMap<>();

    private long lastIdCounter = 1L;

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            items.put(lastIdCounter, item);
            item.setId(lastIdCounter);
            lastIdCounter++;
        } else {
            items.put(item.getId(), item);
        }
        return item;
    }

    @Override
    public Optional<Item> findByItemId(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> findByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<Item> findByStr(String searchText) {
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchText.toLowerCase())
                        || item.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean contains(long itemId) {
        return items.containsKey(itemId);
    }

}
