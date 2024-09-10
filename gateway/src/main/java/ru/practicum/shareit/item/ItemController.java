package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
	private final ItemClient itemClient;

	@PostMapping
	public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
										  @Valid @RequestBody ItemRequestDto item) {
		log.info("Creating item {}, userId={}", item, userId);
		return itemClient.addNewItem(userId, item);
	}

	@PatchMapping("/{itemId}")
	public ResponseEntity<Object> change(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @PathVariable(name = "itemId") Long itemId,
										 @RequestBody ItemRequestDto item) {
		log.info("Change item {}, userId={}, itemId={}", item, userId, itemId);
		return itemClient.changeItem(userId, itemId, item);
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
										  @PathVariable(name = "itemId") Long itemId) {
		log.info("Get item, userId={}, itemId={}", userId, itemId);
		return itemClient.getItem(userId, itemId);
	}

	@GetMapping
	public ResponseEntity<Object> getUserItem(@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Get user item, userId={}", userId);
		return itemClient.getUserItem(userId);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
											  @RequestParam(required = false) String text) {
		log.info("Search items, userId={}, findString={}", userId, text);
		return itemClient.searchItems(userId, text);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
											 @PathVariable(name = "itemId") Long itemId,
											 @Valid @RequestBody CommentRequestDto commentDto) {
		log.info("Add comment {}, userId={}, itemId={}", commentDto, userId, itemId);
		return itemClient.addNewComment(userId, itemId, commentDto);
	}
}
