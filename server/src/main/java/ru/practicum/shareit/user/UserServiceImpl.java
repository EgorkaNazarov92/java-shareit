package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.ExistException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Override
	@Transactional
	public UserDto saveUser(UserDto userDto) {
		if (repository.getByEmail(userDto.getEmail()).isPresent())
			throw new ExistException("Такой email уже есть");
		User user = UserMapper.mapToUser(userDto);
		return UserMapper.mapToUserDto(repository.save(user));
	}

	@Override
	@Transactional
	public UserDto changeUser(Long userId, UserDto userDto) {
		User user = UserMapper.mapToUser(userDto);
		User gboUser = getUserFromRepo(userId);
		if (!gboUser.getEmail().equals(user.getEmail()) && repository.getByEmail(userDto.getEmail()).isPresent())
			throw new ExistException("Такой email уже есть");
		if (user.getName() != null) gboUser.setName(user.getName());
		if (user.getEmail() != null) gboUser.setEmail(user.getEmail());
		return UserMapper.mapToUserDto(repository.save(gboUser));
	}

	@Override
	public UserDto getUser(Long id) {
		return UserMapper.mapToUserDto(getUserFromRepo(id));
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		repository.deleteById(id);
	}

	private User getUserFromRepo(Long userId) {
		Optional<User> user = repository.findById(userId);
		if (user.isEmpty()) throw new NotFoundException("Пользователя с id = " + userId.toString() + " не существует");
		return user.get();
	}
}
