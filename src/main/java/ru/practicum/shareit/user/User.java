package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    /** Уникальный идентификатор пользователя */
    private Long id;

    /** Имя пользователя */
    private String name;

    /** Адрес электронной почты */
    private String email;

}
