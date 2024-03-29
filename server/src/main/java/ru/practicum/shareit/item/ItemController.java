package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemExtendedInfoDtoOut;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDtoOut addItem(@RequestBody ItemDtoIn dto,
                              @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.addItem(dto, userId);
    }

    @PatchMapping("{itemId}")
    public ItemDtoOut updateItem(@RequestBody ItemDtoIn dto,
                                 @PathVariable("itemId") long itemId,
                                 @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.updateItem(dto, itemId, userId);
    }

    @GetMapping("{itemId}")
    public ItemExtendedInfoDtoOut getItem(@PathVariable("itemId") long itemId,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemExtendedInfoDtoOut> getItems(@RequestHeader(USER_ID_HEADER) long userId,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "20") int size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("search")
    public Collection<ItemDtoOut> search(@RequestHeader(USER_ID_HEADER) long userId,
                                         @RequestParam("text") String searchText,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "20") int size) {
        return itemService.search(searchText, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentDtoOut addComment(@RequestBody CommentDtoIn dto,
                                    @RequestHeader(USER_ID_HEADER) long userId,
                                    @PathVariable("itemId") long itemId) {
        return itemService.addComment(dto, userId, itemId);
    }

}
