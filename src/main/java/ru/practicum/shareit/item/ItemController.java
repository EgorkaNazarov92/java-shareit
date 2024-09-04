package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
	private final ItemService itemService;

	@PostMapping
	public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto item) {
		return itemService.addNewItem(userId, item);
	}

	@PatchMapping("/{itemId}")
	public ItemDto change(@RequestHeader("X-Sharer-User-Id") Long userId,
						  @PathVariable(name = "itemId") Long itemId, @RequestBody ItemDto item) {
		return itemService.changeItem(userId, itemId, item);
	}

	@GetMapping("/{itemId}")
	public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
						   @PathVariable(name = "itemId") Long itemId) {
		return itemService.getItem(userId, itemId);
	}

	@GetMapping
	public List<ItemDto> getUserItem(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return itemService.getUserItem(userId);
	}

	@GetMapping("/search")
	public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
									 @RequestParam(required = false) String text) {
		return itemService.searchItems(userId, text);
	}

	@PostMapping("/{itemId}/comment")
	public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
								 @PathVariable(name = "itemId") Long itemId,
								 @Valid @RequestBody CommentDto commentDto) {
		return itemService.addNewComment(userId, itemId, commentDto);
	}
}
