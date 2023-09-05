package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDtoValidator implements ConstraintValidator<StartBeforeEndDateValid, BookDto> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        LocalDateTime now = LocalDateTime.now();
        if (start == null || end == null) {
            return false;
        }
        return now.isBefore(start) && start.isBefore(end);
    }
}
