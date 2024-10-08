package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Service
public class UserClient extends BaseClient {
	private static final String API_PREFIX = "/users";

	@Autowired
	public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> saveUser(UserRequestDto user) {
		validateUser(user);
		return post("", user);
	}

	public ResponseEntity<Object> changeUser(Long userId, UserRequestDto user) {
		return patch("/" + userId, user);
	}

	public ResponseEntity<Object> getUser(Long userId) {
		return get("/" + userId);
	}

	public ResponseEntity<Object> deleteUser(Long userId) {
		return delete("/" + userId);
	}

	private void validateUser(UserRequestDto user) {
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new ValidationException("Имя не может быть пустым");
		}
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new ValidationException("email не может быть пустым");
		}
	}
}
