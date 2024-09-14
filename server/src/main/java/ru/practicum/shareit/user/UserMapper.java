package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
	public static User mapToUser(UserDto userDto) {
		User user = User.builder().build();
		user.setId(userDto.getId());
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		return user;
	}

	public static UserDto mapToUserDto(User user) {
		return new UserDto(
				user.getId(),
				user.getName(),
				user.getEmail()
		);
	}

	public static List<UserDto> mapToUserDto(Iterable<User> users) {
		List<UserDto> dtos = new ArrayList<>();
		for (User user : users) {
			dtos.add(mapToUserDto(user));
		}
		return dtos;
	}
}
