package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	@Mock
	private UserService userService;

	@InjectMocks
	private UserController controller;

	private final ObjectMapper mapper = new ObjectMapper();

	private MockMvc mvc;

	private UserDto userDto;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders
				.standaloneSetup(controller)
				.build();

		userDto = new UserDto(
				1L,
				"John",
				"john.doe@mail.com");
	}

	@Test
	void saveNewUser() throws Exception {
		when(userService.saveUser(any()))
				.thenReturn(userDto);

		mvc.perform(post("/users")
						.content(mapper.writeValueAsString(userDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(userDto.getName())))
				.andExpect(jsonPath("$.email", is(userDto.getEmail())));
	}

	@Test
	void changeUser() throws Exception {
		when(userService.changeUser(userDto.getId(), userDto))
				.thenReturn(userDto);

		mvc.perform(patch("/users/" + userDto.getId())
						.content(mapper.writeValueAsString(userDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(userDto.getName())))
				.andExpect(jsonPath("$.email", is(userDto.getEmail())));
	}

	@Test
	void getUser() throws Exception {
		when(userService.getUser(userDto.getId()))
				.thenReturn(userDto);

		mvc.perform(get("/users/" + userDto.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(userDto.getName())))
				.andExpect(jsonPath("$.email", is(userDto.getEmail())));
	}

	@Test
	void deleteUser() throws Exception {
		userService.deleteUser(userDto.getId());

		mvc.perform(delete("/users/" + userDto.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}