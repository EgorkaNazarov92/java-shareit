package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.ExistException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
	private final List<User> users = new ArrayList<>();

	@Override
	public User save(User user) {
		users.forEach(usr -> {
			if (usr.getEmail().equals(user.getEmail())) throw new ExistException("Такой Email уже используется");
		});
		user.setId(getId());
		users.add(user);
		return user;
	}

	@Override
	public Optional<User> change(Long userId, User user) {
		users.forEach(usr -> {
			if (usr.getEmail().equals(user.getEmail()) && !usr.getId().equals(userId))
				throw new ExistException("Такой Email уже используется");
		});
		return users.stream()
				.filter(userItem -> userId.equals(userItem.getId()))
				.peek(userItem -> {
					if (user.getName() != null) userItem.setName(user.getName());
					if (user.getEmail() != null) userItem.setEmail(user.getEmail());
				}).findFirst();
	}

	@Override
	public Optional<User> get(Long id) {
		return users.stream()
				.filter(user -> user.getId().equals(id))
				.findFirst();
	}

	@Override
	public void delete(Long id) {
		users.stream()
				.filter(user -> user.getId().equals(id))
				.findFirst()
				.map(users::remove);
	}

	private long getId() {
		long lastId = users.stream()
				.mapToLong(User::getId)
				.max()
				.orElse(0);
		return lastId + 1;
	}
}
