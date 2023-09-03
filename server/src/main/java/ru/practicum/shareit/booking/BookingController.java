package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut addBooking(@RequestBody @Valid BookingDtoIn dto,
                                    @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.addBooking(dto, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingDtoOut approveBooking(@PathVariable("bookingId") long bookingId,
                                        @RequestParam("approved") boolean approved,
                                        @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("{bookingId}")
    public BookingDtoOut getBooking(@PathVariable("bookingId") long bookingId,
                                    @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDtoOut> getAllBookingsByUser(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "20") @Positive int size) {
        return bookingService.getAllBookingsByUser(state, userId, from, size);
    }

    @GetMapping("owner")
    public Collection<BookingDtoOut> getAllBookingsByOwner(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "20") @Positive int size) {
        return bookingService.getAllBookingsByOwner(state, userId, from, size);
    }
}
