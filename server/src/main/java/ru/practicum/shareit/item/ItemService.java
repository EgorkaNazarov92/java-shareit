package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
	ItemDto addNewItem(Long userId, ItemDto item);

	ItemDto changeItem(Long userId, Long itemId, ItemDto item);

	ItemDto getItem(Long userId, Long itemId);

	List<ItemDto> getUserItem(Long userId);

	List<ItemDto> searchItems(Long userId, String text);

	CommentDto addNewComment(Long userId, Long itemId, CommentDto commentDto);
}
