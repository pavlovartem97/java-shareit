package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.user.User;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestControllerTest extends BaseTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    @SneakyThrows
    public void addRequest_Success() {
        User user = createUser("name", "email@gmail.com");
        RequestDtoIn requestDtoIn = new RequestDtoIn("description");

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(requestDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is((requestDtoIn.getDescription()))));
    }

    @Test
    @SneakyThrows
    public void addRequest_whenUserDoesNotFound_Failed() {
        RequestDtoIn requestDtoIn = new RequestDtoIn("description");

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(requestDtoIn)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getRequests_Success() {
        User user = createUser("name", "email@gmail.com");
        Request request = createRequest(user);

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is((request.getDescription()))));
    }

    @Test
    @SneakyThrows
    public void getRequests_whenUserDoesNotFond_Failed() {
        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getRequestsAll_Success() {
        User user = createUser("name", "email@gmail.com");
        User otherUser = createUser("name2", "email2@gmail.com");
        Request request = createRequest(user);

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, otherUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is((request.getDescription()))));
    }

    @Test
    @SneakyThrows
    public void getRequestsAll_whenUserIsNotFound_Failed() {
        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getRequestById_Success() {
        User user = createUser("name", "email@gmail.com");
        User otherUser = createUser("name2", "email2@gmail.com");
        Request request = createRequest(user);

        mockMvc.perform(get("/requests/{requestId}", request.getId())
                        .header(USER_ID_HEADER, otherUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is((request.getDescription()))));
    }

    @Test
    @SneakyThrows
    public void getRequestById_whenUserNotFound_Failed() {
        User user = createUser("name", "email@gmail.com");
        User otherUser = createUser("name2", "email2@gmail.com");
        Request request = createRequest(user);

        mockMvc.perform(get("/requests/{requestId}", request.getId())
                        .header(USER_ID_HEADER, 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getRequestById_whenRequestNotFound_Failed() {
        User user = createUser("name", "email@gmail.com");
        User otherUser = createUser("name2", "email2@gmail.com");
        Request request = createRequest(user);

        mockMvc.perform(get("/requests/{requestId}", 999)
                        .header(USER_ID_HEADER, otherUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getRequestById_whenOtherUserCreateItem_Success() {
        User user = createUser("name", "email@gmail.com");
        User otherUser = createUser("name2", "email2@gmail.com");
        Request request = createRequest(user);
        Item item = createItem(user, "name", true, request);

        mockMvc.perform(get("/requests/{requestId}", request.getId())
                        .header(USER_ID_HEADER, otherUser.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description", is((request.getDescription()))))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }
}
