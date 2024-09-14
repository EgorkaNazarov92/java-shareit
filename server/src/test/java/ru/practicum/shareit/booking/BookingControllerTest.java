package ru.practicum.shareit.booking;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
	@Mock
	private BookingService bookingService;

	@InjectMocks
	private BookingController controller;

	private final ObjectMapper mapper = new ObjectMapper();

	private MockMvc mvc;

	private BookingDtoRequest bookingDtoRequest;
	private BookingDtoResponse bookingDtoResponse;
	private User user;
	private Item item;

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

		item = Item.builder().build();
		item.setId(1L);
		item.setName("Пила");
		item.setDescription("Ручная");
		item.setAvailable(true);
		item.setUser(user);

		bookingDtoRequest = new BookingDtoRequest(
				1L,
				item.getId(),
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1),
				BookingStatus.WAITING
		);

		bookingDtoResponse = new BookingDtoResponse(
				1L,
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1),
				BookingStatus.WAITING,
				item,
				user);
	}

	@Test
	void add() throws Exception {
		when(bookingService.addNewBooking(user.getId(), bookingDtoRequest))
				.thenReturn(bookingDtoResponse);

		mvc.perform(post("/bookings")
						.header("X-Sharer-User-Id", user.getId())
						.content(mapper.writeValueAsString(bookingDtoRequest))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));
	}

	@Test
	void approve() throws Exception {
		when(bookingService.approveBooking(user.getId(), bookingDtoResponse.getId(), true))
				.thenReturn(bookingDtoResponse);

		mvc.perform(patch("/bookings/" + bookingDtoResponse.getId() + "?approved=true")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));
	}

	@Test
	void getBooking() throws Exception {
		when(bookingService.getBooking(user.getId(), bookingDtoResponse.getId()))
				.thenReturn(bookingDtoResponse);

		mvc.perform(get("/bookings/" + bookingDtoResponse.getId())
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));
	}

	@Test
	void getBookings() throws Exception {
		when(bookingService.getBookings(user.getId(), "ALL"))
				.thenReturn(List.of(bookingDtoResponse));

		ResultActions resultActions = mvc.perform(get("/bookings?state=ALL")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(bookingDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.[0].status", is(bookingDtoResponse.getStatus().toString())));
	}

	@Test
	void getOwnerBookings() throws Exception {
		when(bookingService.getOwnerBookings(user.getId(), "ALL"))
				.thenReturn(List.of(bookingDtoResponse));

		ResultActions resultActions = mvc.perform(get("/bookings/owner?state=ALL")
						.header("X-Sharer-User-Id", user.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(bookingDtoResponse.getId()), Long.class))
				.andExpect(jsonPath("$.[0].status", is(bookingDtoResponse.getStatus().toString())));
	}
}