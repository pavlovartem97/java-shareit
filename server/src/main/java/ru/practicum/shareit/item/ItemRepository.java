package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.Request;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * " +
            "FROM sh_item " +
            "WHERE user_id = :userId " +
            "ORDER BY id " +
            "LIMIT :limit " +
            "OFFSET :offset", nativeQuery = true)
    Collection<Item> findByUserId(@Param("userId") long userId,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    Collection<Item> findByRequestIn(Collection<Request> request);

    @Query(value = "SELECT * " +
            "FROM sh_item " +
            "WHERE available = TRUE " +
            "    AND (LOWER(name) LIKE LOWER(CONCAT('%',:searchText,'%')) " +
            "        OR LOWER(description) LIKE LOWER(CONCAT('%',:searchText,'%'))) " +
            "ORDER BY id " +
            "LIMIT :limit " +
            "OFFSET :offset", nativeQuery = true)
    Collection<Item> searchByNameAndDescription(@Param("searchText") String searchText,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

}
