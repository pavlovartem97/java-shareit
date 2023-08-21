package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDtoOut;

import java.util.Collection;

@Mapper
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    public abstract User map(UserCreateDto userDto);

    public abstract UserDtoOut map(User user);

    public abstract Collection<UserDtoOut> map(Collection<User> users);

}
