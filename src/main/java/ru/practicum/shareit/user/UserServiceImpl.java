package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Override
	@Transactional
	public UserDto saveUser(UserDto userDto) {
		validateUser(userDto);
		User user = UserMapper.mapToUser(userDto);
		return UserMapper.mapToUserDto(repository.save(user));
	}

	@Override
	@Transactional
	public UserDto changeUser(Long userId, UserDto userDto) {
		User user = UserMapper.mapToUser(userDto);
		User gboUser = repository.getById(userId);
		if (user.getName() != null) gboUser.setName(user.getName());
		if (user.getEmail() != null) gboUser.setEmail(user.getEmail());
		return UserMapper.mapToUserDto(repository.save(gboUser));
	}

	@Override
	public UserDto getUser(Long id) {
		return UserMapper.mapToUserDto(repository.getById(id));
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		repository.deleteById(id);
	}

	private void validateUser(UserDto user) {
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new ValidationException("Имя не может быть пустым");
		}
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new ValidationException("email не может быть пустым");
		}
	}
}
