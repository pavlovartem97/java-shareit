package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item it " +
            "JOIN FETCH it.user " +
            "WHERE b.id = :id")
    Optional<Booking> findByIdFetched(@Param("id") long id);

    @Query(value = "SELECT b.id AS id, " +
            "    b.booker_id AS bookerId " +
            "FROM sh_booking b " +
            "WHERE b.item_id = :itemId " +
            "    AND b.start_date < :now " +
            "    AND b.status = 'APPROVED' " +
            "ORDER BY b.end_date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<BriefInfoView> findLastBriefBookingInfo(@Param("itemId") long itemId,
                                                     @Param("now") LocalDateTime now);

    @Query(value = "SELECT b.id AS id, " +
            "    b.booker_id AS bookerId " +
            "FROM sh_booking b " +
            "WHERE b.item_id = :itemId " +
            "    AND b.start_date > :now " +
            "    AND b.status = 'APPROVED' " +
            "ORDER BY b.start_date " +
            "LIMIT 1", nativeQuery = true)
    Optional<BriefInfoView> findNextBriefBookingInfo(@Param("itemId") long itemId,
                                                     @Param("now") LocalDateTime now);

    boolean existsBookingByBookerAndItemAndStatusAndStartLessThanEqual(User booker,
                                                                       Item item,
                                                                       BookingStatus status,
                                                                       LocalDateTime start);

    interface BriefInfoView {
        long getId();

        long getBookerId();
    }
}
