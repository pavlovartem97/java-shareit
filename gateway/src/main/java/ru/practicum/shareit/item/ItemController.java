package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.utils.Constraints.USER_ID_HEADER;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemCreateDto dto,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.addItem(dto, userId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody @Valid ItemUpdateDto dto,
                                             @PathVariable("itemId") long itemId,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.updateItem(dto, itemId, userId);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable("itemId") long itemId,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(USER_ID_HEADER) long userId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "20") @Positive int size) {
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("search")
    public ResponseEntity<Object> search(@RequestHeader(USER_ID_HEADER) long userId,
                                         @RequestParam("text") String searchText,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "20") @Positive int size) {
        if (searchText.isBlank()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        return itemClient.search(searchText, userId, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDtoIn dto,
                                             @RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable("itemId") long itemId) {
        return itemClient.addComment(dto, userId, itemId);
    }
}
