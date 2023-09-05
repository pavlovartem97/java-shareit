package ru.practicum.shareit.item.dto;

import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class ItemDtoIn {
    /**
     * Краткое название
     */
    @NotEmpty
    @Size(max = 255)
    String name;

    /**
     * Развернутое описание
     */
    @NotEmpty
    @Size(max = 5000)
    String description;

    /**
     * Статус о том, доступна или нет вещь для аренды
     */
    @NotNull
    Boolean available;

    @Nullable
    Long requestId;
}
