package ru.practicum.shareit.item.dto;

import lombok.Value;

@Value
public class ItemDtoIn {
    /**
     * Краткое название
     */
    String name;

    /**
     * Развернутое описание
     */
    String description;

    /**
     * Статус о том, доступна или нет вещь для аренды
     */
    Boolean available;

    Long requestId;
}
