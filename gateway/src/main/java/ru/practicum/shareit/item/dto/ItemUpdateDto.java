package ru.practicum.shareit.item.dto;

import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;

@Value
public class ItemUpdateDto {
    /**
     * Краткое название
     */
    @Nullable
    @Size(max = 255)
    String name;

    /**
     * Развернутое описание
     */
    @Nullable
    @Size(max = 5000)
    String description;

    /**
     * Статус о том, доступна или нет вещь для аренды
     */
    @Nullable
    Boolean available;

    @Nullable
    Long requestId;
}
