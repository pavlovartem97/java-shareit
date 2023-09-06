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
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOut;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDtoOut addUser(@RequestBody UserDtoIn userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("{userId}")
    public UserDtoOut updateUser(@RequestBody UserDtoIn userDto,
                                 @PathVariable("userId") long userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("{userId}")
    public UserDtoOut getUser(@PathVariable("userId") long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public Collection<UserDtoOut> getAllUser() {
        return userService.getAllUsers();
    }

}
