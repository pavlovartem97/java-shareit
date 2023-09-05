package ru.practicum.shareit.user.dto;

import lombok.Value;

@Value
public class UserDtoIn {
    /**
     * Имя пользователя
     */
    String name;

    /**
     * Адрес электронной почты
     */
    String email;
}
