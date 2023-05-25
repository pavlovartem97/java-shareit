package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Value
public class UserCreateDto {

    /** Имя пользователя */
    @NotEmpty(message = "User name is null or empty")
    String name;

    /** Адрес электронной почты */
    @Email(message = "User email is not valid")
    @NotEmpty(message = "User name is null or empty")
    String email;

}
