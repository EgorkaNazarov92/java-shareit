package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestDtoResponse;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class RequestServiceImplTest {

	@Autowired
	private RequestService requestService;

	@Autowired
	private UserService userService;

	@Test
	void addNewRequest() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		RequestDtoRequest requestDto = makeRequestDto("Пила");

		RequestDtoResponse result = requestService.addNewRequest(userDto.getId(), requestDto);

		assertThat(result.getId(), notNullValue());
		assertThat(result.getDescription(), equalTo(requestDto.getDescription()));
	}

	@Test
	void getUserRequests() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		RequestDtoRequest requestDto = makeRequestDto("Пила");

		List<RequestDtoResponse> requests = requestService.getUserRequests(userDto.getId());
		assertTrue(requests.size() == 0);

		requestService.addNewRequest(userDto.getId(), requestDto);


		requests = requestService.getUserRequests(userDto.getId());
		assertTrue(requests.size() == 1);
	}

	@Test
	void getAllRequests() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		RequestDtoRequest requestDto = makeRequestDto("Пила");

		requestService.addNewRequest(userDto.getId(), requestDto);

		List<RequestDtoResponse> requests = requestService.getAllRequests(userDto.getId());
		assertTrue(requests.size() > 0);
	}

	@Test
	void getRequest() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		RequestDtoRequest requestDto = makeRequestDto("Пила");

		RequestDtoResponse result = requestService.addNewRequest(userDto.getId(), requestDto);

		RequestDtoResponse resultGet = requestService.getRequest(userDto.getId(), result.getId());
		assertThat(resultGet, equalTo(result));
	}

	private UserDto makeUserDto(String email, String name) {
		UserDto dto = new UserDto();
		dto.setEmail(email);
		dto.setName(name);

		return dto;
	}

	private RequestDtoRequest makeRequestDto(String desc) {
		RequestDtoRequest dto = new RequestDtoRequest();
		dto.setDescription(desc);

		return dto;
	}
}