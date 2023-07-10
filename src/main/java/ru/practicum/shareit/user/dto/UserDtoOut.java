package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@AllArgsConstructor
public class UserDtoOut {

    /** Уникальный идентификатор пользователя */
    @NonNull
    private Long id;

    /** Имя пользователя */
    @NonNull
    private String name;

    /** Адрес электронной почты */
    @NonNull
    private String email;

}
