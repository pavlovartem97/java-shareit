package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDtoOut;

import java.util.Collection;

@Mapper
public abstract class UserMapper {
    public abstract User map(UserCreateDto userDto);

    public abstract UserDtoOut map(User user);

    public abstract Collection<UserDtoOut> map(Collection<User> users);

}
