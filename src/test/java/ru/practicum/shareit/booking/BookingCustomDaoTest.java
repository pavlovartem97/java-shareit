package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.dto.BookingSearchState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookingCustomDaoTest extends BaseTest {
    @Autowired
    BookingCustomRepository bookingCustomRepository;

    @Test
    public void findAllBookingsByBooker_RejectedAndWaiting_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking rejectedBooking = createBooking(booker, item, BookingStatus.REJECTED, localDateTime.plusDays(1),
                localDateTime.plusDays(2));
        Booking waitedBooking = createBooking(booker, item, BookingStatus.WAITING, localDateTime.minusDays(1),
                localDateTime.plusDays(2));

        assertBookings(rejectedBooking, BookingSearchState.REJECTED, owner, false);
        assertBookings(waitedBooking, BookingSearchState.WAITING, owner, false);
        assertBookings(rejectedBooking, BookingSearchState.REJECTED, booker, true);
        assertBookings(waitedBooking, BookingSearchState.WAITING, booker, true);
    }

    @Test
    public void findAllBookingsByBooker_PastFutureCurrent_Success() {
        User owner = createUser("name", "email@gmail.com");
        User booker = createUser("booker", "booker@gmail.com");
        Item item = createItem(owner, "name", true);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking future = createBooking(booker, item, BookingStatus.REJECTED, localDateTime.plusDays(1),
                localDateTime.plusDays(2));
        Booking current = createBooking(booker, item, BookingStatus.WAITING, localDateTime.minusDays(1),
                localDateTime.plusDays(2));
        Booking past = createBooking(booker, item, BookingStatus.WAITING, localDateTime.minusDays(2),
                localDateTime.minusDays(1));

        assertBookings(future, BookingSearchState.FUTURE, owner, false);
        assertBookings(past, BookingSearchState.PAST, owner, false);
        assertBookings(current, BookingSearchState.CURRENT, owner, false);
        assertBookings(future, BookingSearchState.FUTURE, booker, true);
        assertBookings(past, BookingSearchState.PAST, booker, true);
        assertBookings(current, BookingSearchState.CURRENT, booker, true);
    }

    private void assertBookings(Booking booking, BookingSearchState bookingSearchState, User user, Boolean isBooker) {
        List<Booking> bookings = bookingCustomRepository.findAllBookingsByBooker(user, bookingSearchState, isBooker, 0, 20).stream()
                .collect(Collectors.toUnmodifiableList());
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(booking.getId(), bookings.get(0).getId());
    }
}
