package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingBriefDtoOut;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemExtendedInfoDtoOut;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper(imports = {LocalDateTime.class})
public abstract class ItemMapper {

    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "id", ignore = true)
    public abstract Item map(ItemDtoIn dto, User user);

    public abstract ItemDtoOut map(Item item);

    public abstract Collection<ItemDtoOut> map(Collection<Item> items);

    @Mapping(target = "id", source = "item.id")
    public abstract ItemExtendedInfoDtoOut map(Item item,
                                               @Nullable BookingBriefDtoOut lastBooking,
                                               @Nullable BookingBriefDtoOut nextBooking,
                                               List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "dto.text")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    public abstract Comment map(CommentDtoIn dto, User author, Item item);

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "authorName", source = "comment.author.name")
    public abstract CommentDtoOut map(Comment comment);

}
