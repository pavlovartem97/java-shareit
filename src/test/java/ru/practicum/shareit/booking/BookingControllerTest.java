package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookingControllerTest extends BaseTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    @SneakyThrows
    public void addBooking_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);

        LocalDateTime localDateTime = LocalDateTime.now();
        BookingDtoIn bookingDtoIn = new BookingDtoIn(localDateTime.plusDays(1), localDateTime.plusDays(2), item.getId());

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, booker.getId())
                        .content(toJsonBytes(bookingDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.description", is(item.getDescription())))
                .andExpect(jsonPath("$.item.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(booker.getName())))
                .andExpect(jsonPath("$.booker.email", is(booker.getEmail())))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.name())));
    }

    @Test
    @SneakyThrows
    public void addBooking_WhenDatesWrong_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);

        LocalDateTime localDateTime = LocalDateTime.now();
        BookingDtoIn bookingDtoIn = new BookingDtoIn(localDateTime.plusDays(2), localDateTime.plusDays(1), item.getId());

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, booker.getId())
                        .content(toJsonBytes(bookingDtoIn)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void approveBooking_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.WAITING, localDateTime.plusDays(1),
                localDateTime.plusDays(2));

        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .header(USER_ID_HEADER, owner.getId())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.description", is(item.getDescription())))
                .andExpect(jsonPath("$.item.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(booker.getName())))
                .andExpect(jsonPath("$.booker.email", is(booker.getEmail())))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.name())));
    }

    @Test
    @SneakyThrows
    public void rejectBooking_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.WAITING, localDateTime.plusDays(1),
                localDateTime.plusDays(2));

        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .header(USER_ID_HEADER, owner.getId())
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.description", is(item.getDescription())))
                .andExpect(jsonPath("$.item.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(booker.getName())))
                .andExpect(jsonPath("$.booker.email", is(booker.getEmail())))
                .andExpect(jsonPath("$.status", is(BookingStatus.REJECTED.name())));
    }

    @Test
    @SneakyThrows
    public void getBooking_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.WAITING, localDateTime.plusDays(1),
                localDateTime.plusDays(2));

        mockMvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .header(USER_ID_HEADER, owner.getId())
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.description", is(item.getDescription())))
                .andExpect(jsonPath("$.item.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(booker.getName())))
                .andExpect(jsonPath("$.booker.email", is(booker.getEmail())))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.name())));
    }

    @Test
    @SneakyThrows
    public void getBookingByUser_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = createBooking(booker, item, BookingStatus.WAITING, localDateTime.plusDays(1),
                localDateTime.plusDays(2));

        mockMvc.perform(get("/bookings/")
                        .header(USER_ID_HEADER, booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(item.getName())))
                .andExpect(jsonPath("$[0].item.description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(item.getAvailable())))
                .andExpect(jsonPath("$[0].booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.name", is(booker.getName())))
                .andExpect(jsonPath("$[0].booker.email", is(booker.getEmail())))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.name())));
    }

    @Test
    @SneakyThrows
    public void getBookingByOwner_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        createBooking(booker, item, BookingStatus.WAITING, localDateTime.plusDays(1),
                localDateTime.plusDays(2));

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(item.getName())))
                .andExpect(jsonPath("$[0].item.description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(item.getAvailable())))
                .andExpect(jsonPath("$[0].booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.name", is(booker.getName())))
                .andExpect(jsonPath("$[0].booker.email", is(booker.getEmail())))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.name())));
    }
}
