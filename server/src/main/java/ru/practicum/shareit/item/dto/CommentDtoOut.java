package ru.practicum.shareit.item.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentDtoOut {
    /**
     * Идентификатор комментария
     */
    Long id;

    /**
     * Текст комментария
     */
    String text;

    /**
     * Дата создания
     */
    LocalDateTime created;

    /**
     * Автор комментария
     */
    String authorName;

}
