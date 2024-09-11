package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
	private final UserClient userClient;

	@PostMapping
	public ResponseEntity<Object> saveNewUser(@Valid @RequestBody UserRequestDto user) {
		log.info("Creating user {}", user);
		return userClient.saveUser(user);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<Object> changeUser(@PathVariable(name = "userId") Long userId,
											 @RequestBody UserRequestDto user) {
		log.info("Change user {}, userid={}", user, userId);
		return userClient.changeUser(userId, user);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Object> getUser(@PathVariable(name = "userId") Long userId) {
		log.info("Get user userid={}", userId);
		return userClient.getUser(userId);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") Long userId) {
		log.info("Delete user userid={}", userId);
		return userClient.deleteUser(userId);
	}
}
