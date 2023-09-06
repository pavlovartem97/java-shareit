package ru.practicum.shareit.user.dto;

import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Value
public class UserUpdateDto {
    /**
     * Имя пользователя
     */
    @Nullable
    @Size(max = 255)
    String name;

    /**
     * Адрес электронной почты
     */
    @Email(message = "User email is not valid")
    @Nullable
    @Size(max = 255)
    String email;
}
