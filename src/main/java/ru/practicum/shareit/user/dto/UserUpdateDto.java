package ru.practicum.shareit.user.dto;

import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;

@Value
public class UserUpdateDto {
    /** Имя пользователя */
    @Nullable
    String name;

    /** Адрес электронной почты */
    @Email(message = "User email is not valid")
    @Nullable
    String email;
}
