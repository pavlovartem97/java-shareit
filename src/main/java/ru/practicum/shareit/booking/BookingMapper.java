package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Mapper
public abstract class BookingMapper {

    @Mapping(target = "id", ignore = true)
    public abstract Booking map(BookingDtoIn dto, Item item, User booker, BookingStatus status);

    public abstract BookingDtoOut map(Booking booking);

}
