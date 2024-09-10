package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
	UserDto saveUser(UserDto user);

	UserDto changeUser(Long userId, UserDto user);

	UserDto getUser(Long id);

	void deleteUser(Long id);
}
