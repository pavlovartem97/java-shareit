package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingBriefDtoOut;
import ru.practicum.shareit.item.dto.ItemBookingBriefInfoDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Mapper
public abstract class ItemMapper {

    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "id", ignore = true)
    public abstract Item map(ItemDtoIn dto, User user);

    public abstract ItemDtoOut map(Item item);

    public abstract Collection<ItemDtoOut> map(Collection<Item> items);

    @Mapping(target = "id", source = "item.id")
    public abstract ItemBookingBriefInfoDtoOut map(Item item,
                                                   @Nullable BookingBriefDtoOut lastBooking,
                                                   @Nullable BookingBriefDtoOut nextBooking);
}
