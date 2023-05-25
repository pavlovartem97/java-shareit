package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Value
public class UserCreateDto {

    /** Имя пользователя */
    @NotEmpty
    String name;

    /** Адрес электронной почты */
    @Email
    @NotEmpty
    String email;

}
