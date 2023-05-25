package ru.practicum.shareit.item.dto;

import lombok.Value;
import org.springframework.lang.NonNull;

@Value
public class ItemDtoOut {

    /** Идентификатор предмета */
    @NonNull
    Long id;

    /** Краткое название */
    @NonNull
    String name;

    /** Развернутое описание */
    @NonNull
    String description;

    /** Статус о том, доступна или нет вещь для аренды */
    @NonNull
    Boolean available;

}
