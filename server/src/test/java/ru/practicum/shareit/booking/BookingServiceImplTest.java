package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class BookingServiceImplTest {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

	@Test
	void addNewBooking() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		UserDto userDto2 = userService.saveUser(makeUserDto("some22@email.com", "Вася"));
		BookingDtoRequest bookingDto = makeBookingDtoRequest(userDto);

		BookingDtoResponse result = bookingService.addNewBooking(userDto2.getId(), bookingDto);

		assertThat(result.getId(), notNullValue());
		assertThat(result.getStatus(), equalTo(BookingStatus.WAITING));
		assertThat(result.getItem().getId(), equalTo(bookingDto.getItemId()));
	}

	@Test
	void approveBooking() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		UserDto userDto2 = userService.saveUser(makeUserDto("some22@email.com", "Вася"));
		BookingDtoRequest bookingDto = makeBookingDtoRequest(userDto);

		BookingDtoResponse result = bookingService.addNewBooking(userDto2.getId(), bookingDto);

		assertThat(result.getStatus(), equalTo(BookingStatus.WAITING));
		result = bookingService.approveBooking(userDto.getId(), result.getId(), true);
		assertThat(result.getStatus(), equalTo(BookingStatus.APPROVED));
	}

	@Test
	void getBooking() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		UserDto userDto2 = userService.saveUser(makeUserDto("some22@email.com", "Вася"));
		BookingDtoRequest bookingDto = makeBookingDtoRequest(userDto);

		BookingDtoResponse result = bookingService.addNewBooking(userDto2.getId(), bookingDto);

		BookingDtoResponse getResult = bookingService.getBooking(userDto.getId(), result.getId());
		assertThat(result, equalTo(getResult));

	}

	@Test
	void getBookings() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		UserDto userDto2 = userService.saveUser(makeUserDto("some22@email.com", "Вася"));
		BookingDtoRequest bookingDto = makeBookingDtoRequest(userDto);

		bookingService.addNewBooking(userDto2.getId(), bookingDto);
		List<BookingDtoResponse> bookingDtoResponses = bookingService.getBookings(userDto2.getId(), "ALL");
		assertTrue(bookingDtoResponses.size() > 0);
	}

	@Test
	void getOwnerBookings() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		UserDto userDto2 = userService.saveUser(makeUserDto("some22@email.com", "Вася"));
		BookingDtoRequest bookingDto = makeBookingDtoRequest(userDto);

		bookingService.addNewBooking(userDto2.getId(), bookingDto);
		List<BookingDtoResponse> bookingDtoResponses = bookingService.getOwnerBookings(userDto.getId(), "ALL");
		assertTrue(bookingDtoResponses.size() == 1);
	}


	private BookingDtoRequest makeBookingDtoRequest(UserDto userDto) {

		ItemDto itemDto = itemService.addNewItem(userDto.getId(), makeItemDto("Пила", "Ручная"));

		BookingDtoRequest dto = new BookingDtoRequest();
		dto.setItemId(itemDto.getId());
		dto.setStart(LocalDateTime.now().plusHours(1));
		dto.setEnd(LocalDateTime.now().plusDays(1));

		return dto;
	}

	private UserDto makeUserDto(String email, String name) {
		UserDto dto = new UserDto();
		dto.setEmail(email);
		dto.setName(name);

		return dto;
	}

	private ItemDto makeItemDto(String name, String desc) {
		ItemDto dto = new ItemDto();
		dto.setName(name);
		dto.setDescription(desc);
		dto.setAvailable(true);

		return dto;
	}
}