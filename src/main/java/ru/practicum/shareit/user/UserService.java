package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserService {
	User saveUser(User user);

	Optional<User> changeUser(Long userId, User user);

	Optional<User> getUser(Long id);

	void deleteUser(Long id);
}
