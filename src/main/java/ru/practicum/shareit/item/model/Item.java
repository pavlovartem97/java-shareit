package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Getter
@Setter
public class Item {

    /** Уникальный идентификатор вещи */
    private Long id;

    /** Краткое название */
    private String name;

    /** Развернутое описание */
    private String description;

    /** Статус о том, доступна или нет вещь для аренды */
    private boolean available;

    /** Владелец вещи */
    private User owner;

    /** Запрос на другую вещь в случае создания по запросу */
    private ItemRequest itemRequest;

}
