package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {
	User save(User user);

	Optional<User> change(Long userId, User user);

	Optional<User> get(Long id);

	void delete(Long id);
}
