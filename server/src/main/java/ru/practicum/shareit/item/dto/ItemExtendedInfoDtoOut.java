package ru.practicum.shareit.item.dto;

import lombok.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingBriefDtoOut;

import java.util.List;

@Value
public class ItemExtendedInfoDtoOut {
    /**
     * Идентификатор предмета
     */
    @NonNull
    Long id;

    /**
     * Краткое название
     */
    @NonNull
    String name;

    /**
     * Развернутое описание
     */
    @NonNull
    String description;

    /**
     * Статус о том, доступна или нет вещь для аренды
     */
    @NonNull
    Boolean available;

    /**
     * Предыдущее бронирование
     */
    @Nullable
    BookingBriefDtoOut lastBooking;

    /**
     * Следующее бронирование
     */
    @Nullable
    BookingBriefDtoOut nextBooking;

    /**
     * Комментарии к посту
     */
    @NonNull
    List<CommentDtoOut> comments;
}
