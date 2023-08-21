package ru.practicum.shareit.request.dto;

import lombok.Value;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.time.LocalDateTime;
import java.util.Collection;

@Value
public class RequestDtoOut {
    Long id;

    String description;

    LocalDateTime created;

    Collection<ItemDtoOut> items;
}
