package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Repository
public class UserDaoInMemoryImpl implements UserDao {

    private final Map<Long, User> users = new TreeMap<>();
    private long lastIdCounter = 1L;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            users.put(lastIdCounter, user);
            user.setId(lastIdCounter);
            lastIdCounter++;
        } else {
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public Optional<User> findByUserId(long userId) {
        return Optional.ofNullable(users.getOrDefault(userId, null));
    }

    @Override
    public void deleteByUserId(long userId) {
        users.remove(userId);
    }

    @Override
    public Set<String> findAllEmail() {
        return users.values().stream().map(User::getEmail).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

}
