package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

    User save(User user);

    Optional<User> findByUserId(long userId);

    void deleteByUserId(long userId);

    boolean existByEmail(String email);

    Collection<User> findAll();

    boolean contains(long userId);

}
