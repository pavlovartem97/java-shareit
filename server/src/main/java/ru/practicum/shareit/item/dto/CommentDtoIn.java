package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CommentDtoIn {
    /**
     * Текст комментария
     */
    String text;

    @JsonCreator
    public CommentDtoIn(@JsonProperty("text") String text) {
        this.text = text;
    }
}
