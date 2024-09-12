package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class UserServiceImplTest {

	@Autowired
	private UserService userService;

	@Test
	void saveUser() {
		// given
		UserDto userDto = makeUserDto("some2@email.com", "Пётр");

		// when
		UserDto result = userService.saveUser(userDto);

		assertThat(result.getId(), notNullValue());
		assertThat(result.getName(), equalTo(userDto.getName()));
		assertThat(result.getEmail(), equalTo(userDto.getEmail()));
	}

	@Test
	void changeUser() {
		// given
		UserDto userDto = makeUserDto("some2@email.com", "Пётр");

		// when
		UserDto result = userService.saveUser(userDto);

		result.setName("Вася");
		result.setEmail("test@email.com");

		UserDto newResult = userService.changeUser(result.getId(), result);

		assertThat(newResult.getId(), equalTo(result.getId()));
		assertThat(newResult.getName(), equalTo("Вася"));
		assertThat(newResult.getEmail(), equalTo("test@email.com"));
	}

	@Test
	void getUser() {
		// given
		UserDto userDto = makeUserDto("some2@email.com", "Пётр");

		// when
		UserDto result = userService.saveUser(userDto);

		UserDto getResult = userService.getUser(result.getId());

		assertEquals(getResult, result);
	}

	@Test
	void deleteUser() {
		// given
		UserDto userDto = makeUserDto("some2@email.com", "Пётр");

		// when
		UserDto result = userService.saveUser(userDto);

		UserDto getResult = userService.getUser(result.getId());

		assertEquals(getResult, result);
		userService.deleteUser(result.getId());
		assertThatThrownBy(() -> userService.getUser(result.getId()))
				.isInstanceOf(NotFoundException.class);
	}

	private UserDto makeUserDto(String email, String name) {
		UserDto dto = new UserDto();
		dto.setEmail(email);
		dto.setName(name);

		return dto;
	}
}