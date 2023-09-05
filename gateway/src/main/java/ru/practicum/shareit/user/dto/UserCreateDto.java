package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class UserCreateDto {

    /**
     * Имя пользователя
     */
    @NotEmpty(message = "User name is null or empty")
    @Size(max = 255)
    String name;

    /**
     * Адрес электронной почты
     */
    @Email(message = "User email is not valid")
    @NotEmpty(message = "User name is null or empty")
    @Size(max = 512)
    String email;

}
