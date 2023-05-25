package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExist;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    public User addUser(UserCreateDto dto) {
        checkUniqueEmail(dto.getEmail());
        User user = userMapper.map(dto);
        return userDao.save(user);
    }

    public void deleteUser(long userId) {
        userDao.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        userDao.deleteByUserId(userId);
    }

    public User updateUser(UserUpdateDto dto, long userId) {
        final User user = userDao.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        if ((dto.getEmail() == null || dto.getEmail().equals(user.getEmail()))
                && (dto.getName() == null || dto.getName().equals(user.getName()))) {
            return user;
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            checkUniqueEmail(dto.getEmail());
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null && !dto.getName().equals(user.getName())) {
            user.setName(dto.getName());
        }
        return userDao.save(user);
    }

    public User getUser(long userId) {
        return userDao.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    public Collection<User> getAllUsers() {
        return userDao.findAll();
    }

    private void checkUniqueEmail(String email) {
        Set<String> emails = userDao.findAllEmail();
        if (emails.contains(email)) {
            throw new UserEmailAlreadyExist(String.format("User email %s already exist", email));
        }
    }
}
