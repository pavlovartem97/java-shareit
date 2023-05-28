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
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDtoOut addItem(@RequestBody @Valid ItemDtoIn dto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(dto, userId);
    }

    @PatchMapping("{itemId}")
    public ItemDtoOut updateItem(@RequestBody ItemDtoIn dto,
                                 @PathVariable("itemId") long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(dto, itemId, userId);
    }

    @GetMapping("{itemId}")
    public ItemDtoOut getItem(@PathVariable("itemId") long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public Collection<ItemDtoOut> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("search")
    public Collection<ItemDtoOut> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam("text") String str) {
        return itemService.search(str);
    }

}
