package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Mapper
public abstract class ItemMapper {

    @Mapping(target = "owner", source = "user")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "id", ignore = true)
    public abstract Item map(ItemDtoIn dto, User user);

    public abstract ItemDtoOut map(Item item);

    public abstract Collection<ItemDtoOut> map(Collection<Item> items);

}
