package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
	User save(User user);

	User change(Long userId, User user);

	User get(Long id);

	void delete(Long id);
}
