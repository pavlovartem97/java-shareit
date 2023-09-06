package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class RequestDtoIn {
    String description;

    @JsonCreator
    public RequestDtoIn(@JsonProperty("description") String description) {
        this.description = description;
    }
}
