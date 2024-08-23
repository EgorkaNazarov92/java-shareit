package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
	private final UserService userService;

	@PostMapping
	public User saveNewUser(@Valid @RequestBody User user) {
		return userService.saveUser(user);
	}

	@PatchMapping("/{userId}")
	public Optional<User> changeUser(@PathVariable(name = "userId") Long userId, @RequestBody User user) {
		return userService.changeUser(userId, user);
	}

	@GetMapping("/{userId}")
	public Optional<User> getUser(@PathVariable(name = "userId") Long userId) {
		return userService.getUser(userId);
	}

	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable(name = "userId") Long userId) {
		userService.deleteUser(userId);
	}
}
