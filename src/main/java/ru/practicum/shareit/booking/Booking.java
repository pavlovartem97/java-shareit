package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class Booking {

    /**
     * Уникальный идентификатор бронирования
     */
    private Long id;

    /**
     * Дата и время начала бронирования
     */
    private LocalDateTime start;

    /**
     * Дата и время окончания бронирования
     */
    private LocalDateTime end;

    /**
     * Вещь, которую пользователь бронирует
     */
    private Item item;

    /**
     * Пользователь, осуществляющий бронирование
     */
    private User booker;

    /**
     * Статус бронирования
     */
    private BookingStatus status;

}
