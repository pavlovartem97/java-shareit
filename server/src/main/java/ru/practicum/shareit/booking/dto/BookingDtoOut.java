package ru.practicum.shareit.booking.dto;

import lombok.Value;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDtoOut;

import java.time.LocalDateTime;

@Value
public class BookingDtoOut {

    @NonNull
    Long id;

    @NonNull
    LocalDateTime start;

    @NonNull
    LocalDateTime end;

    @NonNull
    ItemDtoOut item;

    @NonNull
    UserDtoOut booker;

    @NonNull
    BookingStatus status;

}
