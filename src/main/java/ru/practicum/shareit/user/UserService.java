package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDtoOut;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExist;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    public UserDtoOut addUser(UserCreateDto dto) {
        checkUniqueEmail(dto.getEmail());
        User user = userMapper.map(dto);
        userDao.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(long userId) {
        if (!userDao.contains(userId)) {
            throw new UserNotFoundException("User is not found");
        }
        userDao.deleteByUserId(userId);
    }

    public UserDtoOut updateUser(UserUpdateDto dto, long userId) {
        final User user = userDao.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        if ((dto.getEmail() == null || dto.getEmail().equals(user.getEmail()))
                && (dto.getName() == null || dto.getName().equals(user.getName()))) {
            return userMapper.map(user);
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            checkUniqueEmail(dto.getEmail());
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null && !dto.getName().equals(user.getName())) {
            user.setName(dto.getName());
        }
        userDao.save(user);
        return userMapper.map(user);
    }

    public UserDtoOut getUser(long userId) {
        User user = userDao.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        return userMapper.map(user);
    }

    public Collection<UserDtoOut> getAllUsers() {
        Collection<User> users = userDao.findAll();
        return userMapper.map(users);
    }

    private void checkUniqueEmail(String email) {
        if (userDao.existByEmail(email)) {
            throw new UserEmailAlreadyExist(String.format("User email %s already exist", email));
        }
    }
}
