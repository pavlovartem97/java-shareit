package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDtoValidator implements ConstraintValidator<BookDtoValid, BookDto> {
    @Override
    public void initialize(BookDtoValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        LocalDateTime now = LocalDateTime.now();
        if (start == null || end == null || bookingDto.getItemId() == null) {
            return false;
        }
        return (now.isBefore(start) || now.equals(start)) && start.isBefore(end);
    }
}
