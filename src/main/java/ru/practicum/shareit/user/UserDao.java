package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserDao {

    User save(User user);

    Optional<User> findByUserId(long userId);

    void deleteByUserId(long userId);

    Set<String> findAllEmail();

    Collection<User> findAll();

}
