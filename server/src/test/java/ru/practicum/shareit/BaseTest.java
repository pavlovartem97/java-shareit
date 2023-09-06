package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Profile("test")
public abstract class BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected RequestRepository requestRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    public byte[] toJsonBytes(Object object) {
        return mapper.writeValueAsBytes(object);
    }

    public User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public Item createItem(User user, String name, Boolean available) {
        return createItem(user, name, available, null);
    }

    public Item createItem(User user, String name, Boolean available, Request request) {
        Item item = new Item();
        item.setName(name);
        item.setDescription("description");
        item.setAvailable(available);
        item.setUser(user);
        item.setRequest(request);
        return itemRepository.save(item);
    }

    public Booking createBooking(User user,
                                 Item item,
                                 BookingStatus bookingStatus,
                                 LocalDateTime start,
                                 LocalDateTime end) {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(bookingStatus);
        booking.setStart(start);
        booking.setEnd(end);
        return bookingRepository.save(booking);
    }

    public Request createRequest(User user) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequestor(user);
        request.setDescription("description");
        return requestRepository.save(request);
    }

    public Comment createComment(User user, Item item) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText("text");
        return commentRepository.save(comment);
    }
}
