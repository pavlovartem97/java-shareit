package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public class CommentDtoIn {
    /**
     * Текст комментария
     */
    @NotEmpty
    String text;

    @JsonCreator
    public CommentDtoIn(@JsonProperty("text") String text) {
        this.text = text;
    }

    ;
}
