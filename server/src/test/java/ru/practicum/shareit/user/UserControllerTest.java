package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseTest {

    @Test
    @SneakyThrows
    public void addUser_Success() {
        UserCreateDto userCreateDto = new UserCreateDto("name", "email@gmail.com");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userCreateDto.getName())))
                .andExpect(jsonPath("$.email", is(userCreateDto.getEmail())));
    }

    @Test
    public void addUser_UserEmailAlreadyExists_Failed() throws Exception {
        String existEmail = "email@gmail.com";
        createUser("name", existEmail);

        UserCreateDto userCreateDto = new UserCreateDto("name", existEmail);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userCreateDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void addUser_invalidEmail_Failed() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("name", "thiIsNoEmail");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUser_invalidName_Failed() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto(null, "email@gmail.com");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUser_Success() throws Exception {
        User user = createUser("name", "email@gmail.com");
        UserUpdateDto userUpdateDto = new UserUpdateDto("newName", "newEmail@gmail.com");
        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userUpdateDto.getName())))
                .andExpect(jsonPath("$.email", is(userUpdateDto.getEmail())));

        user = userRepository.getById(user.getId());
        Assertions.assertEquals(userUpdateDto.getEmail(), user.getEmail());
        Assertions.assertEquals(userUpdateDto.getName(), user.getName());
    }

    @Test
    public void updateUser_sameFields_Success() throws Exception {
        User user = createUser("name", "email@gmail.com");
        UserUpdateDto userUpdateDto = new UserUpdateDto(user.getName(), user.getEmail());
        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userUpdateDto.getName())))
                .andExpect(jsonPath("$.email", is(userUpdateDto.getEmail())));

        user = userRepository.getById(user.getId());
        Assertions.assertEquals(userUpdateDto.getEmail(), user.getEmail());
        Assertions.assertEquals(userUpdateDto.getName(), user.getName());
    }

    @Test
    public void updateUser_nullValues_Success() throws Exception {
        User user = createUser("name", "email@gmail.com");
        UserUpdateDto userUpdateDto = new UserUpdateDto(null, null);
        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void updateUser_updateOnlyName_Success() throws Exception {
        User user = createUser("name", "email@gmail.com");
        UserUpdateDto userUpdateDto = new UserUpdateDto("name", "newEmail@gmail.com");
        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void deleteUser_Success() throws Exception {
        User user = createUser("name", "email@gmail.com");

        mockMvc.perform(delete("/users/{userId}", user.getId()))
                .andExpect(status().isOk());

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    public void getUser_Success() throws Exception {
        User user = createUser("name", "email@gmail.com");

        mockMvc.perform(get("/users/{userId}", user.getId()))
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void getAllUsers_Success() throws Exception {
        User user1 = createUser("name", "email@gmail.com");
        User user2 = createUser("name2", "email2@gmail.com");

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail())));
    }
}
