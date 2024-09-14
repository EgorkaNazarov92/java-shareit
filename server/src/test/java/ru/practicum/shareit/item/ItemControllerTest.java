package ru.practicum.shareit.item;

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
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
	@Mock
	private ItemService itemService;

	@InjectMocks
	private ItemController controller;

	private final ObjectMapper mapper = new ObjectMapper();

	private MockMvc mvc;

	private User user;
	private ItemDto item;
	private CommentDto commentDto;

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

		item = new ItemDto(
				1L,
				"Пила",
				"Ручная",
				true,
				LocalDateTime.now().minusDays(1),
				LocalDateTime.now().plusDays(1),
				new HashSet<String>(),
				1L);

		commentDto = new CommentDto(
				1L,
				"Отличная",
				user.getName(),
				LocalDateTime.now()
		);
	}

	@Test
	void add() throws Exception {
		when(itemService.addNewItem(user.getId(), item))
				.thenReturn(item);

		mvc.perform(post("/items")
						.header("X-Sharer-User-Id", user.getId())
						.content(mapper.writeValueAsString(item))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(item.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(item.getName())))
				.andExpect(jsonPath("$.description", is(item.getDescription())));
	}

	@Test
	void change() throws Exception {
		when(itemService.changeItem(user.getId(), item.getId(), item))
				.thenReturn(item);

		mvc.perform(patch("/items/" + item.getId())
						.header("X-Sharer-User-Id", user.getId())
						.content(mapper.writeValueAsString(item))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(item.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(item.getName())))
				.andExpect(jsonPath("$.description", is(item.getDescription())));
	}

	@Test
	void getItem() throws Exception {
		when(itemService.getItem(user.getId(), item.getId()))
				.thenReturn(item);

		mvc.perform(get("/items/" + item.getId())
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(item.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(item.getName())))
				.andExpect(jsonPath("$.description", is(item.getDescription())));
	}

	@Test
	void getUserItem() throws Exception {
		when(itemService.getUserItem(user.getId()))
				.thenReturn(List.of(item));

		mvc.perform(get("/items")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(item.getId()), Long.class))
				.andExpect(jsonPath("$.[0].name", is(item.getName())))
				.andExpect(jsonPath("$.[0].description", is(item.getDescription())));
	}

	@Test
	void searchItems() throws Exception {
		when(itemService.searchItems(user.getId(), "Пила"))
				.thenReturn(List.of(item));

		mvc.perform(get("/items/search?text=Пила")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(item.getId()), Long.class))
				.andExpect(jsonPath("$.[0].name", is(item.getName())))
				.andExpect(jsonPath("$.[0].description", is(item.getDescription())));
	}

	@Test
	void addComment() throws Exception {
		when(itemService.addNewComment(user.getId(), item.getId(), commentDto))
				.thenReturn(commentDto);

		mvc.perform(post("/items/" + item.getId() + "/comment")
						.header("X-Sharer-User-Id", user.getId())
						.content(mapper.writeValueAsString(commentDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
				.andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
				.andExpect(jsonPath("$.text", is(commentDto.getText())));
	}
}