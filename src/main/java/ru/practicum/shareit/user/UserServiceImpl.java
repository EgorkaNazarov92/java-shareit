package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Override
	public UserDto saveUser(UserDto userDto) {
		validateUser(userDto);
		User user = UserMapper.mapToUser(userDto);
		return UserMapper.mapToUserDto(repository.save(user));
	}

	@Override
	public UserDto changeUser(Long userId, UserDto userDto) {
		User user = UserMapper.mapToUser(userDto);
		return UserMapper.mapToUserDto(repository.change(userId, user));
	}

	@Override
	public UserDto getUser(Long id) {
		return UserMapper.mapToUserDto(repository.get(id));
	}

	@Override
	public void deleteUser(Long id) {
		repository.delete(id);
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
