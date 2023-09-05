package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOut;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDtoOut addUser(UserDtoIn dto) {
        User user = userMapper.map(dto);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @Transactional
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        userRepository.delete(user);
    }

    @Transactional
    public UserDtoOut updateUser(UserDtoIn dto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        if ((dto.getEmail() == null || dto.getEmail().equals(user.getEmail()))
                && (dto.getName() == null || dto.getName().equals(user.getName()))) {
            return userMapper.map(user);
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null && !dto.getName().equals(user.getName())) {
            user.setName(dto.getName());
        }
        userRepository.save(user);
        return userMapper.map(user);
    }

    @Transactional(readOnly = true)
    public UserDtoOut getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        return userMapper.map(user);
    }

    @Transactional(readOnly = true)
    public Collection<UserDtoOut> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        return userMapper.map(users);
    }
}
