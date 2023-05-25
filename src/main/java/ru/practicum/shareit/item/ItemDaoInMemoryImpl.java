package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ItemDaoInMemoryImpl implements ItemDao {

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
        return Optional.ofNullable(items.getOrDefault(itemId, null));
    }

    @Override
    public Collection<Item> findByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<Item> findByStr(String str) {
        String regex = "*" + str + "*";
        return items.values().stream()
                .filter(item -> item.isAvailable())
                .filter(item -> Pattern.matches(regex, item.getName())
                        || Pattern.matches(regex, item.getDescription()))
                .collect(Collectors.toUnmodifiableList());
    }

}
