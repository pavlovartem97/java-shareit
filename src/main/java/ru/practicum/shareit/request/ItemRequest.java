package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequest {

    /**
     * Уникальный идентификатор запроса
     */
    private Long id;


    /**
     * Описание запроса
     */
    private String string;

    /**
     * Пользователь, создавший запрос
     */
    private User requestor;

    /**
     * Дата и время создания запроса
     */
    private LocalDateTime created;

}
