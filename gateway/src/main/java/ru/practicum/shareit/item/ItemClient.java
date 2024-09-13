package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
	private static final String API_PREFIX = "/items";

	@Autowired
	public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> addNewItem(Long userId, ItemRequestDto item) {
		validateItem(item);
		return post("", userId, item);
	}

	public ResponseEntity<Object> changeItem(Long userId, Long itemId, ItemRequestDto item) {
		return patch("/" + itemId, userId, item);
	}

	public ResponseEntity<Object> getItem(Long userId, Long itemId) {
		return get("/" + itemId, userId);
	}

	public ResponseEntity<Object> getUserItem(Long userId) {
		return get("", userId);
	}

	public ResponseEntity<Object> searchItems(Long userId, String text) {
		Map<String, Object> parameters = Map.of("text", text);
		return get("/search", userId, parameters);
	}

	public ResponseEntity<Object> addNewComment(Long userId, Long itemId, CommentRequestDto commentDto) {
		return post("/" + itemId + "/comment", userId, commentDto);
	}

	private void validateItem(ItemRequestDto itemDto) {
		if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
			throw new ValidationException("Имя не может быть пустым");
		}
		if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
			throw new ValidationException("Description не может быть пустым");
		}
	}
}
