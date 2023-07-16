package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT " +
            "CASE " +
            "    WHEN COUNT(u) = 0 " +
            "    THEN FALSE " +
            "    ELSE TRUE " +
            "END " +
            "FROM User u " +
            "WHERE u.email = :email " +
            "    AND u.id != :id")
    boolean existsUserByEmail(String email, long id);

}
