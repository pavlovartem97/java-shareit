package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTest extends BaseTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    @SneakyThrows
    public void addItem_Success() {
        User user = createUser("name", "email@gmail.com");
        ItemDtoIn dto = new ItemDtoIn("name", "description", true, null);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.available", is(dto.getAvailable())));
    }

    @Test
    @SneakyThrows
    public void addItem_whenRequestDoesNotExist_Failed() {
        User user = createUser("name", "email@gmail.com");
        ItemDtoIn dto = new ItemDtoIn("name", "description", true, 999L);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void addItem_whenItemDoesNotExist_Failed() {
        ItemDtoIn dto = new ItemDtoIn("name", "description", true, null);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void updateItem_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);
        ItemDtoIn dto = new ItemDtoIn("newName", "newDescription", true, null);

        mockMvc.perform(patch("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.available", is(dto.getAvailable())));
    }

    @Test
    @SneakyThrows
    public void updateItem_whenAllFieldsNull_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);
        ItemDtoIn dto = new ItemDtoIn(null, null, null, null);

        mockMvc.perform(patch("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    public void updateItem_whenOtherUser_Failed() {
        User user = createUser("name", "email@gmail.com");
        User otherUser = createUser("other", "other@gmail.com");
        Item item = createItem(user, "name", true);
        ItemDtoIn dto = new ItemDtoIn("newName", "newDescription", true, null);

        mockMvc.perform(patch("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, otherUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getItem_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);

        mockMvc.perform(get("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    public void getItems_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);
        Item item2 = createItem(user, "name2", false);
        User user2 = createUser("name", "email2@gmail.com");
        User user3 = createUser("name", "email3@gmail.com");
        createComment(user2, item2);
        createComment(user3, item);

        mockMvc.perform(get("/items/")
                        .header(USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())))
                .andExpect(jsonPath("$[1].name", is(item2.getName())))
                .andExpect(jsonPath("$[1].description", is(item2.getDescription())))
                .andExpect(jsonPath("$[1].available", is(item2.getAvailable())));
    }

    @Test
    @SneakyThrows
    public void getItems_UserIsNotExists_Failed() {
        mockMvc.perform(get("/items/")
                        .header(USER_ID_HEADER, 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getItem_whenItemDoesNotExists_Failed() {
        User user = createUser("name", "email@gmail.com");
        mockMvc.perform(get("/items/{itemId}", 999)
                        .header(USER_ID_HEADER, user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void search_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);

        mockMvc.perform(get("/items/search")
                        .header(USER_ID_HEADER, user.getId())
                        .param("text", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    public void search_searchTextIsBlank_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "", true);

        mockMvc.perform(get("/items/search")
                        .header(USER_ID_HEADER, user.getId())
                        .param("text", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @SneakyThrows
    public void addComment_success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.APPROVED, localDateTime.minusDays(2),
                localDateTime.minusDays(1));

        CommentDtoIn dto = new CommentDtoIn("text");
        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header(USER_ID_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void addComment_whenUserExist_Failed() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.APPROVED, localDateTime.minusDays(2),
                localDateTime.minusDays(1));

        CommentDtoIn dto = new CommentDtoIn("text");
        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header(USER_ID_HEADER, 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void addComment_whenItemDoesNotExist_Failed() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.APPROVED, localDateTime.minusDays(2),
                localDateTime.minusDays(1));

        CommentDtoIn dto = new CommentDtoIn("text");
        mockMvc.perform(post("/items/{itemId}/comment", 999)
                        .header(USER_ID_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @EnumSource(names = "APPROVED", mode = EnumSource.Mode.EXCLUDE)
    @SneakyThrows
    public void addComment_whenStatusWrong_Failed(BookingStatus bookingStatus) {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, bookingStatus, localDateTime.minusDays(2),
                localDateTime.minusDays(1));

        CommentDtoIn dto = new CommentDtoIn("text");
        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header(USER_ID_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void getItem_withComments_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);
        User otherUser = createUser("name2", "email2@gmail.com");
        Comment comment = createComment(otherUser, item);

        mockMvc.perform(get("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.comments[0].text", is(comment.getText())))
                .andExpect(jsonPath("$.comments[0].authorName", is(otherUser.getName())));
    }

    @Test
    @SneakyThrows
    public void getItem_withPreviousAndNextBooking_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);
        User nextBooker = createUser("name2", "email2@gmail.com");
        User prevBooker = createUser("name3", "email3@gmail.com");
        LocalDateTime now = LocalDateTime.now();
        Booking prevBooking = createBooking(prevBooker, item, BookingStatus.APPROVED, now.minusDays(2), now.minusDays(1));
        Booking nextBooking = createBooking(nextBooker, item, BookingStatus.APPROVED, now.plusDays(1), now.plusDays(2));

        mockMvc.perform(get("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, user.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(prevBooking.getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(prevBooker.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(nextBooking.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(nextBooker.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    public void getItem_withPreviousAndNextBookingByOtherBooker_Success() {
        User user = createUser("name", "email@gmail.com");
        Item item = createItem(user, "name", true);
        User nextBooker = createUser("name2", "email2@gmail.com");
        User prevBooker = createUser("name3", "email3@gmail.com");
        LocalDateTime now = LocalDateTime.now();
        createBooking(prevBooker, item, BookingStatus.APPROVED, now.minusDays(2), now.minusDays(1));
        createBooking(nextBooker, item, BookingStatus.APPROVED, now.plusDays(1), now.plusDays(2));

        mockMvc.perform(get("/items/{itemId}", item.getId())
                        .header(USER_ID_HEADER, nextBooker.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", nullValue()))
                .andExpect(jsonPath("$.nextBooking", nullValue()));
    }
}
