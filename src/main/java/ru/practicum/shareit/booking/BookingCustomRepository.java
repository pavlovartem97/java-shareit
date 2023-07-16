package ru.practicum.shareit.booking;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingSearchState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public class BookingCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Collection<Booking> findAllBookingsByBooker(User user, BookingSearchState state, boolean isBooker) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> query = builder.createQuery(Booking.class);
        Root<Booking> root = query.from(Booking.class);

        Path<LocalDateTime> end = root.get("end");
        Path<LocalDateTime> start = root.get("start");

        LocalDateTime now = LocalDateTime.now();
        Predicate predicate = null;

        switch (state) {
            case WAITING:
                predicate = builder.equal(root.get("status"), BookingStatus.WAITING);
                break;
            case PAST:
                predicate = builder.lessThan(end, now);
                break;
            case FUTURE:
                predicate = builder.greaterThan(start, now);
                break;
            case CURRENT:
                predicate = builder.and(builder.lessThan(start, now), builder.greaterThan(end, now));
                break;
            case REJECTED:
                predicate = builder.equal(root.get("status"), BookingStatus.REJECTED);
                break;
        }

        Predicate userPredicate = null;

        if (isBooker) {
            Join<Booking, User> join = root.join("booker");
            userPredicate = builder.equal(join, user);
        } else {
            Join<Item, User> itemJoin = root.join("item").join("user");
            userPredicate = builder.equal(itemJoin, user);
        }

        if (predicate == null) {
            query.where(userPredicate);
        } else {
            query.where(userPredicate, predicate);
        }

        query.orderBy(builder.desc(start));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
