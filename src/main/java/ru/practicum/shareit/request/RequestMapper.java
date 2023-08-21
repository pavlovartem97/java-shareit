package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Mapper
public abstract class RequestMapper {

    @Mapping(target = "id", ignore = true)
    public abstract Request map(RequestDtoIn requestDtoIn, User requestor);

    public abstract RequestDtoOut map(Request request, Collection<ItemDtoOut> items);

    @Mapping(target = "items", ignore = true)
    public abstract RequestDtoOut map(Request request);
}
