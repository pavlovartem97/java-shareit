package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findByUser(User user);

    Collection<Item> findByRequestIn(Collection<Request> request);

    @Query("SELECT it " +
            "FROM Item it " +
            "WHERE it.available = TRUE " +
            "    AND (LOWER(it.name) LIKE LOWER(CONCAT('%',:searchText,'%')) " +
            "        OR LOWER(it.description) LIKE LOWER(CONCAT('%',:searchText,'%')))")
    Collection<Item> searchByNameAndDescription(@Param("searchText") String searchText);

}
