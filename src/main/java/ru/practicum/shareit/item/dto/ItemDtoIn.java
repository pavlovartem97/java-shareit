package ru.practicum.shareit.item.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class ItemDtoIn {
    /**
     * Краткое название
     */
    @NotEmpty
    String name;

    /**
     * Развернутое описание
     */
    @NotEmpty
    String description;

    /**
     * Статус о том, доступна или нет вещь для аренды
     */
    @NotNull
    Boolean available;
}
