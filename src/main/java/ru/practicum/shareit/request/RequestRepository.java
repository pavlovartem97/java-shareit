package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findByRequestorOrderByCreated(User requestor);

    @Query(value = "SELECT * " +
            "FROM sh_request " +
            "WHERE NOT requestor_id = :userId " +
            "ORDER BY created DESC " +
            "LIMIT :size " +
            "OFFSET :offset ", nativeQuery = true)
    Collection<Request> findRequestsAll(@Param("userId") long userId,
                                        @Param("offset") int offset,
                                        @Param("size") int size);


}
