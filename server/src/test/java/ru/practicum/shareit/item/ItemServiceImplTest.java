package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.comment.dto.CommentDto;
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
class ItemServiceImplTest {

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	@Test
	void addNewItem() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		ItemDto itemDto = makeItemDto("Пила", "ручная");

		ItemDto result = itemService.addNewItem(userDto.getId(), itemDto);

		assertThat(result.getId(), notNullValue());
		assertThat(result.getName(), equalTo(itemDto.getName()));
		assertThat(result.getDescription(), equalTo(itemDto.getDescription()));
	}

	@Test
	void changeItem() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		ItemDto itemDto = makeItemDto("Пила", "ручная");

		ItemDto result = itemService.addNewItem(userDto.getId(), itemDto);
		result.setDescription("бензо");

		result = itemService.changeItem(userDto.getId(), result.getId(), result);

		assertThat(result.getId(), notNullValue());
		assertThat(result.getName(), equalTo(itemDto.getName()));
		assertThat(result.getDescription(), equalTo("бензо"));
	}

	@Test
	void getItem() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		ItemDto itemDto = makeItemDto("Пила", "ручная");

		ItemDto result = itemService.addNewItem(userDto.getId(), itemDto);

		ItemDto getResult = itemService.getItem(userDto.getId(), result.getId());

		assertThat(result, equalTo(getResult));
	}

	@Test
	void getUserItem() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		ItemDto itemDto = makeItemDto("Пила", "ручная");

		List<ItemDto> items = itemService.getUserItem(userDto.getId());
		assertTrue(items.size() == 0);

		itemService.addNewItem(userDto.getId(), itemDto);

		items = itemService.getUserItem(userDto.getId());
		assertTrue(items.size() == 1);
	}

	@Test
	void searchItems() {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		ItemDto itemDto = makeItemDto("Пила", "двуручная");

		List<ItemDto> items = itemService.searchItems(userDto.getId(), "двуручная");
		assertTrue(items.size() == 0);

		itemService.addNewItem(userDto.getId(), itemDto);

		items = itemService.searchItems(userDto.getId(), "двуручная");
		assertTrue(items.size() == 1);
	}

	@Test
	void addNewComment() throws InterruptedException {
		UserDto userDto = userService.saveUser(makeUserDto("some2@email.com", "Пётр"));
		UserDto userDto2 = userService.saveUser(makeUserDto("some22@email.com", "Вася"));
		ItemDto itemDto = makeItemDto("Пила", "ручная");

		ItemDto result = itemService.addNewItem(userDto.getId(), itemDto);

		BookingDtoRequest dto = new BookingDtoRequest();
		dto.setItemId(result.getId());
		dto.setStart(LocalDateTime.now().plusSeconds(1));
		dto.setEnd(LocalDateTime.now().plusSeconds(2));

		bookingService.addNewBooking(userDto2.getId(), dto);
		Thread.sleep(4000);

		CommentDto commentDto = new CommentDto();
		commentDto.setAuthorName(userDto2.getName());
		commentDto.setText("Отличная пила");
		CommentDto resultComment = itemService.addNewComment(userDto2.getId(), result.getId(), commentDto);

		assertThat(resultComment.getId(), notNullValue());
		assertThat(resultComment.getText(), equalTo(commentDto.getText()));
		assertThat(resultComment.getAuthorName(), equalTo(commentDto.getAuthorName()));
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