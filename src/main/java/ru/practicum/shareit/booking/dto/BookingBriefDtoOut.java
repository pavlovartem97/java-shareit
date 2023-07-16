package ru.practicum.shareit.booking.dto;

import lombok.Value;
import org.springframework.lang.NonNull;

@Value
public class BookingBriefDtoOut {
    @NonNull
    Long id;
    @NonNull
    Long bookerId;
}
