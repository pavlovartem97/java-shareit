package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.validation.BookDtoValid;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@BookDtoValid
public class BookDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
