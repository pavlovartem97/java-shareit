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

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDtoOut addItem(@RequestBody @Valid ItemDtoIn dto,
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
    public Collection<ItemExtendedInfoDtoOut> getItems(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("search")
    public Collection<ItemDtoOut> search(@RequestHeader(USER_ID_HEADER) long userId,
                                         @RequestParam("text") String searchText) {
        return itemService.search(searchText);
    }

    @PostMapping("{itemId}/comment")
    public CommentDtoOut addComment(@RequestBody @Valid CommentDtoIn dto,
                                    @RequestHeader(USER_ID_HEADER) long userId,
                                    @PathVariable("itemId") long itemId) {
        return itemService.addComment(dto, userId, itemId);
    }

}
