package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingSearchState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.dto.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.dto.BookingStatus.WAITING;
import static ru.practicum.shareit.util.Utils.checkFromAndSize;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final BookingCustomRepository bookingCustomRepository;

    @Transactional
    public BookingDtoOut addBooking(BookingDtoIn dto, long userId) {
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }
        if (item.getUser().getId() == userId) {
            throw new NotFoundException("Owner can't book his item");
        }

        validateDates(dto);

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Booking booking = bookingMapper.map(dto, item, booker, WAITING);

        bookingRepository.save(booking);

        return bookingMapper.map(booking);
    }

    @Transactional
    public BookingDtoOut approveBooking(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findByIdFetched(bookingId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        User owner = booking.getItem().getUser();
        if (owner.getId() != userId) {
            throw new NotFoundException("User is not item's owner");
        }
        if (booking.getStatus() != WAITING) {
            throw new BadRequestException("Booking status already changed");
        }

        booking.setStatus(approved ? APPROVED : REJECTED);

        return bookingMapper.map(booking);
    }

    @Transactional(readOnly = true)
    public BookingDtoOut getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findByIdFetched(bookingId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        User owner = booking.getItem().getUser();
        if (owner.getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("User is not owner or booker");
        }

        return bookingMapper.map(booking);
    }

    @Transactional(readOnly = true)
    public Collection<BookingDtoOut> getAllBookingsByUser(String stringState, long bookerId, int from, int size) {
        checkFromAndSize(from, size);
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Collection<Booking> bookings = bookingCustomRepository
                .findAllBookingsByBooker(booker, getState(stringState), true, from, size);

        return bookings.stream()
                .map(bookingMapper::map)
                .collect(Collectors.toUnmodifiableList());
    }

    public Collection<BookingDtoOut> getAllBookingsByOwner(String stringState, long ownerId, int from, int size) {
        checkFromAndSize(from, size);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Collection<Booking> bookings = bookingCustomRepository.findAllBookingsByBooker(owner,
                getState(stringState), false, from, size);

        return bookings.stream()
                .map(bookingMapper::map)
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateDates(BookingDtoIn dto) {
        if (dto.getEnd().equals(dto.getStart()) || dto.getEnd().isBefore(dto.getStart())) {
            throw new BadRequestException("End date must be after start date");
        }
    }

    private BookingSearchState getState(String string) {
        try {
            return BookingSearchState.valueOf(string);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(String.format("Unknown state: %s", string));
        }
    }
}
