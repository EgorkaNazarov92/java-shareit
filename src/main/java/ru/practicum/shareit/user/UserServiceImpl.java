package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Override
	public User saveUser(User user) {
		validateUser(user);
		return repository.save(user);
	}

	@Override
	public Optional<User> changeUser(Long userId, User user) {
		return repository.change(userId, user);
	}

	@Override
	public Optional<User> getUser(Long id) {
		return repository.get(id);
	}

	@Override
	public void deleteUser(Long id) {
		repository.delete(id);
	}

	private void validateUser(User user) {
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new ValidationException("Имя не может быть пустым и содержать пробелы");
		}
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new ValidationException("email не может быть пустым и содержать пробелы");
		}
	}
}
