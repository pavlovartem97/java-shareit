package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItemOrderByIdDesc(Item item);

    List<Comment> findByItemInOrderById(Collection<Item> item);

}
