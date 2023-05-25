package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserCreateDto;

@Mapper
public abstract class UserMapper {
    public abstract User map(UserCreateDto userDto);
}
