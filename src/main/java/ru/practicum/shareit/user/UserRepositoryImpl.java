package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.ExistException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
	private final Map<Long, User> users = new HashMap<>();

	@Override
	public User save(User user) {
		users.values().forEach(usr -> {
			if (usr.getEmail().equals(user.getEmail())) throw new ExistException("Такой Email уже используется");
		});
		user.setId(getId());
		users.put(user.getId(), user);
		return user;
	}

	@Override
	public User change(Long userId, User newUser) {
		users.values().forEach(usr -> {
			if (usr.getEmail().equals(newUser.getEmail()) && !usr.getId().equals(userId))
				throw new ExistException("Такой Email уже используется");
		});
		User user = get(userId);
		if (newUser.getName() != null) user.setName(newUser.getName());
		if (newUser.getEmail() != null) user.setEmail(newUser.getEmail());
		users.put(userId, user);
		return user;
	}

	@Override
	public User get(Long id) {
		Optional<User> item = Optional.ofNullable(users.get(id));
		if (item.isEmpty()) throw new NotFoundException("Нет пользователя c id = " + id);
		return item.get();
	}

	@Override
	public void delete(Long id) {
		users.remove(id);
	}

	private long getId() {
		long lastId = users.keySet().stream()
				.max(Long::compareTo)
				.orElse(0L);
		return lastId + 1;
	}
}
