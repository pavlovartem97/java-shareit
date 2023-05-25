package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addUser(@RequestBody @Valid UserCreateDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("{userId}")
    public User updateUser(@RequestBody @Valid UserUpdateDto userDto,
                           @PathVariable("userId") long userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable("userId") long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public Collection<User> getAllUser() {
        return userService.getAllUsers();
    }

}
