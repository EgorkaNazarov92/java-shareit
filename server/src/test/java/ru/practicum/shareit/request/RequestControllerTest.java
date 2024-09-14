package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestDtoResponse;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
	@Mock
	private RequestService requestService;

	@InjectMocks
	private RequestController controller;

	private final ObjectMapper mapper = new ObjectMapper();

	private MockMvc mvc;

	private User user;
	private ItemRequestDto item;
	private RequestDtoRequest requestDtoRequest;
	private RequestDtoResponse requestDtoResponse;

	@BeforeEach
	void setUp() {
		mapper.registerModule(new JavaTimeModule());

		mvc = MockMvcBuilders
				.standaloneSetup(controller)
				.build();

		user = User.builder().build();
		user.setId(1L);
		user.setName("John");
		user.setEmail("ohn.doe@mail.com");

		item = new ItemRequestDto(
				1L,
				"Пила");

		requestDtoRequest = new RequestDtoRequest("Пила");

		requestDtoResponse = new RequestDtoResponse(
				1L,
				"Пила",
				LocalDateTime.now(),
				user,
				Set.of(item)
		);
	}

	@Test
	void add() throws Exception {
		when(requestService.addNewRequest(user.getId(), requestDtoRequest))
				.thenReturn(requestDtoResponse);

		mvc.perform(post("/requests")
						.header("X-Sharer-User-Id", user.getId())
						.content(mapper.writeValueAsString(requestDtoRequest))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(requestDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.description", is(requestDtoResponse.getDescription())));
	}

	@Test
	void getUserRequests() throws Exception {
		when(requestService.getUserRequests(user.getId()))
				.thenReturn(List.of(requestDtoResponse));

		mvc.perform(get("/requests")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(requestDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.[0].description", is(requestDtoResponse.getDescription())));
	}

	@Test
	void getAllRequests() throws Exception {
		when(requestService.getAllRequests(user.getId()))
				.thenReturn(List.of(requestDtoResponse));

		mvc.perform(get("/requests/all")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(requestDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.[0].description", is(requestDtoResponse.getDescription())));
	}

	@Test
	void getRequest() throws Exception {
		when(requestService.getRequest(user.getId(), requestDtoResponse.getId()))
				.thenReturn(requestDtoResponse);

		mvc.perform(get("/requests/" + requestDtoResponse.getId())
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(requestDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.description", is(requestDtoResponse.getDescription())));
	}
}