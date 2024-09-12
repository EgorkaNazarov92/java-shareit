package ru.practicum.shareit;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.error.ExistException;
import ru.practicum.shareit.error.ForbiddenException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@Validated
@ActiveProfiles("test")
public class ShareItTests {
	@Autowired
	private UserController userController;
	@Autowired
	private ItemController itemController;
	@Autowired
	private BookingController bookingController;

	private UserDto userAlex1;
	private UserDto userAlex3;
	private UserDto userCustomerName4;
	private UserDto userAnna5;
	private UserDto userWithNullName;
	private UserDto userWithEmptyName;
	private UserDto userWithNullEmail;
	private ItemDto screwDriver;
	private ItemDto lawnMower;
	private ItemDto bike;
	private ItemDto noName;
	private ItemDto adultBike;
	private ItemDto nullDescription;
	private ItemDto nullAvailable;
	private ItemDto onlyAvailable;
	private ItemDto onlyDescription;
	private ItemDto onlyName;

	private BookingDtoRequest bookingItem1Future;
	private BookingDtoRequest bookingItem1Future2;
	private BookingDtoRequest bookingItem1Future3;
	private BookingDtoRequest bookingInvalidStartInPast;
	private BookingDtoRequest bookingInvalidStartEqualsEnd;
	private BookingDtoRequest bookingInvalidEndInPast;
	private BookingDtoRequest bookingInvalidEndBeforeStart;
	private BookingDtoRequest bookingItem2;
	private CommentDto commentToItem1First;
	private CommentDto commentToItem2;
	private CommentDto commentWithEmptyText;
	private CommentDto commentWithoutText;
	private Long nonExistingId;

	ShareItTests() {
	}


	@BeforeEach
	public void create() {
		userAlex1 = UserDto.builder().email("Alex@yandex.ru").name("Alexandr Ivanov").build();
		UserDto userEgor2 = UserDto.builder().email(" ").name("Egor Egorov").build();
		userAlex3 = UserDto.builder().email("Alex@yandex.ru").name("Alexey Petrov").build();
		userCustomerName4 = UserDto.builder().email("CustomerName@yandex.ru").name("CustomerName Smith").build();
		userAnna5 = UserDto.builder().email("Anna@yandex.ru").name("Anna Smith").build();
		userWithEmptyName = UserDto.builder().name("").email("a@yandex.ru").build();
		UserDto userWithInvalidEmail = UserDto.builder().name("Anna").email("email").build();
		userWithNullName = UserDto.builder().email("a@yandex.ru").build();
		userWithNullEmail = UserDto.builder().name("Anna").build();
		screwDriver = ItemDto.builder().name("screwdriver").description("new").available(true).build();
		lawnMower = ItemDto.builder().name("lawn-mower").description("portable").available(false).build();
		bike = ItemDto.builder().name("bike").description("for children").available(true).build();
		adultBike = ItemDto.builder().name("bike").description("adult").available(true).build();
		noName = ItemDto.builder().name("").description("for children").available(true).build();
		nullDescription = ItemDto.builder().name("bike").available(true).build();
		onlyAvailable = ItemDto.builder().available(false).build();
		bookingItem1Future = BookingDtoRequest.builder().itemId(1L)
				.start(LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.end(LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.build();
		bookingItem1Future2 = BookingDtoRequest.builder().itemId(1L)
				.start(LocalDateTime.of(2030, 2, 2, 20, 11, 11))
				.end(LocalDateTime.of(2030, 3, 1, 1, 1, 1))
				.build();
		bookingItem1Future3 = BookingDtoRequest.builder().itemId(1L)
				.start(LocalDateTime.of(2030, 4, 2, 20, 11, 11))
				.end(LocalDateTime.of(2030, 4, 3, 1, 1, 1))
				.build();
		bookingItem2 = BookingDtoRequest.builder().itemId(2L)
				.start(LocalDateTime.of(2030, 1, 1, 1, 1, 1))
				.end(LocalDateTime.of(2030, 1, 2, 1, 1, 1))
				.build();
		bookingInvalidStartEqualsEnd = BookingDtoRequest.builder().itemId(1L)
				.start(LocalDateTime.of(2030, 4, 2, 20, 11, 11))
				.end(LocalDateTime.of(2030, 4, 2, 20, 11, 11))
				.build();
		bookingInvalidEndBeforeStart = BookingDtoRequest.builder().itemId(1L)
				.start(LocalDateTime.of(2030, 12, 1, 1, 1, 1))
				.end(LocalDateTime.of(2030, 1, 1, 1, 1, 1))
				.build();
		bookingInvalidStartInPast = BookingDtoRequest.builder().itemId(1L)
				.start(LocalDateTime.now().minusYears(1))
				.end(LocalDateTime.now())
				.build();
		commentToItem1First = CommentDto
				.builder().id(1L).authorName("Alexey Petrov").text("I like it").build();
		commentToItem2 = CommentDto
				.builder().id(2L).authorName("Alexey Petrov").text("Don't use it").build();
		nonExistingId = -1L;

	}

	@Test
	public void shouldCreateUserAndGetUserById() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		Optional<UserDto> userOptional = Optional.ofNullable(userController.getUser(user1.getId()));
		assertThat(userOptional).hasValueSatisfying(user -> assertThat(user)
				.hasFieldOrPropertyWithValue("id", user.getId())
				.hasFieldOrPropertyWithValue("email", "Alex@yandex.ru")
				.hasFieldOrPropertyWithValue("name", "Alexandr Ivanov"));
	}

	@Test
	public void shouldFailGetUserByInvalidId() {

		final Long userId = -1L;
		assertThrows(NotFoundException.class,
				() -> userController.getUser(userId),
				"Не выброшено исключение NotFoundException.");
	}

	@Test
	public void shouldFailCreateUserWithSameEmail() {

		userController.saveNewUser(userAlex1);
		assertThrows(ExistException.class,
				() -> userController.saveNewUser(userAlex3),
				"Не выброшено исключение DataIntegrityViolationException.");

	}

	@Test
	public void shouldFailGetUserWithNonExistingId() {

		assertThrows(NotFoundException.class,
				() -> userController.getUser(nonExistingId),
				"Не выброшено исключение NotFoundException.");
	}


	@Test
	public void shouldUpdateUser() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		UserDto userAlex1Updated = user1.toBuilder().email("AlexSmith@google.ru")
				.name("Alex Smith").build();
		final Long userId = user1.getId();

		userController.changeUser(userId, userAlex1Updated);
		Optional<UserDto> userOptional = Optional.ofNullable(userController.getUser(userId));
		assertThat(userOptional)
				.hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", user.getId())
						.hasFieldOrPropertyWithValue("email", "AlexSmith@google.ru")
						.hasFieldOrPropertyWithValue("name", "Alex Smith"));

	}

	@Test
	public void shouldFailUpdateUserWithRegisteredByOtherUserEmail() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		UserDto user2 = userController.saveNewUser(userAnna5);
		UserDto user2Updated = user1.toBuilder().email("Alex@yandex.ru")
				.name("Alex Smith").build();
		final Long userId = user2.getId();

		assertThrows(ExistException.class,
				() -> userController.changeUser(userId, user2Updated),
				"Не выброшено исключение ConflictEmailException.");
	}

	@Test
	public void shouldFailUpdateUserWithNonExistingId() {

		UserDto user = userController.saveNewUser(userAlex1);
		UserDto userUpdated = user.toBuilder().email("Alex@yandex.ru")
				.name("Alex Smith").build();

		assertThrows(NotFoundException.class,
				() -> userController.changeUser(nonExistingId, userUpdated),
				"Не выброшено исключение NotFoundException.");
	}

	@Test
	public void shouldDeleteUser() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		final Long userId = user1.getId();
		userController.deleteUser(userId);

		assertThrows(NotFoundException.class,
				() -> userController.getUser(userId),
				"Не выброшено исключение NotFoundException.");

	}

	@Test
	public void shouldListUsers() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		UserDto user4 = userController.saveNewUser(userCustomerName4);

		List<UserDto> listUsers = new ArrayList<>();

		listUsers.add(userController.getUser(user1.getId()));
		listUsers.add(userController.getUser(user4.getId()));

		assertThat(listUsers).asList().hasSize(2);

		assertThat(Optional.of(listUsers.get(0))).hasValueSatisfying(
				user -> AssertionsForClassTypes.assertThat(user)
						.hasFieldOrPropertyWithValue("name", "Alexandr Ivanov"));

		assertThat(Optional.of(listUsers.get(1))).hasValueSatisfying(
				user -> AssertionsForClassTypes.assertThat(user)
						.hasFieldOrPropertyWithValue("name", "CustomerName Smith"));

	}

	@Test
	public void shouldCreateItemAndGetItByIdWithoutApprovedBookings() {

		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		bookingController.add(bookerId, bookingItem1Future);
		Long bookingId = userDto.getId();
		BookingDtoResponse bookingItem = bookingController.approve(ownerId, bookingId, true);

		Optional<ItemDto> itemOptional = Optional.ofNullable(itemController.getItem(ownerId, itemDto.getId()));
		assertThat(itemOptional).hasValueSatisfying(i -> assertThat(i)
				.hasFieldOrPropertyWithValue("id", i.getId())
				.hasFieldOrPropertyWithValue("description", "new")
				.hasFieldOrPropertyWithValue("available", true)
				.hasFieldOrPropertyWithValue("name", "screwdriver"));

	}


	@Test
	public void shouldFailGetItemByNonExistingId() {
		UserDto user = userController.saveNewUser(userAlex1);
		Long userId = user.getId();

		assertThrows(NotFoundException.class,
				() -> itemController.getItem(userId, nonExistingId),
				"Не выброшено исключение NotFoundException.");
	}

	@Test
	public void shouldFailUpdateItemWithNonExistingUserId() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		final Long userId = userDto.getId();
		ItemDto itemDto = itemController.add(userId, screwDriver);
		ItemDto updatedDescriptionItem = itemDto.toBuilder().description("rusty and old").build();

		assertThrows(NotFoundException.class,
				() -> itemController.change(nonExistingId, itemDto.getId(), updatedDescriptionItem),
				"Не выброшено исключение NotFoundException.");

	}

	@Test
	public void shouldFailUpdateItemWithNonExistingId() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		final Long userId = userDto.getId();

		assertThrows(EntityNotFoundException.class,
				() -> itemController.change(userId, nonExistingId, screwDriver),
				"Не выброшено исключение NotFoundException.");

	}

	@Test
	public void shouldFailUpdateItemByNotOwnerId() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		final Long ownerId = userDto.getId();
		UserDto userDto1 = userController.saveNewUser(userAnna5);
		final Long notOwnerId = userDto1.getId();
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		ItemDto updatedDescriptionItem = itemDto.toBuilder().description("rusty and old").build();

		assertThrows(ForbiddenException.class,
				() -> itemController.change(notOwnerId, itemDto.getId(), updatedDescriptionItem),
				"Не выброшено исключение ForbiddenException.");

	}

	@Test
	public void shouldUpdateItem() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		final Long userId = userDto.getId();
		ItemDto itemDto = itemController.add(userId, screwDriver);
		ItemDto updatedDescriptionItem = itemDto.toBuilder().description("rusty and old").build();
		itemController.change(userId, itemDto.getId(), updatedDescriptionItem);

		Optional<ItemDto> itemOptional = Optional.ofNullable(itemController.getItem(userId, itemDto.getId()));
		assertThat(itemOptional).hasValueSatisfying(item -> assertThat(item)
				.hasFieldOrPropertyWithValue("id", item.getId())
				.hasFieldOrPropertyWithValue("description", "rusty and old")
				.hasFieldOrPropertyWithValue("available", true)
				.hasFieldOrPropertyWithValue("name", "screwdriver"));

	}

	@Test
	public void shouldUpdateItemWithAvailableOnly() { // добавление вещи
		UserDto userDto = userController.saveNewUser(userAlex1);
		final Long userId = userDto.getId();
		ItemDto itemDto = itemController.add(userId, screwDriver);
		itemController.change(userId, itemDto.getId(), onlyAvailable);

		Optional<ItemDto> itemOptional = Optional.ofNullable(itemController.getItem(userId, itemDto.getId()));
		assertThat(itemOptional).hasValueSatisfying(item -> assertThat(item)
				.hasFieldOrPropertyWithValue("id", item.getId())
				.hasFieldOrPropertyWithValue("description", "new")
				.hasFieldOrPropertyWithValue("available", false)
				.hasFieldOrPropertyWithValue("name", "screwdriver"));

	}

	@Test
	public void shouldDeleteItem() { // удаление вещи

		UserDto userDto = userController.saveNewUser(userAlex1);
		ItemDto itemOutDto = itemController.add(1L, screwDriver);
		final Long userId = userDto.getId();
		final Long itemId = itemOutDto.getId();
		List<ItemDto> listWithItem = itemController.getUserItem(userId);
		assertThat(listWithItem).asList().hasSize(1);
		assertThat(listWithItem).asList().contains(itemOutDto);

	}

	@Test
	public void shouldListItemsByUser() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		UserDto user4 = userController.saveNewUser(userCustomerName4);
		final Long user1Id = user1.getId();
		final Long user4Id = user4.getId();

		ItemDto item1OutDto = itemController.add(user1Id, screwDriver);
		ItemDto item2OutDto = itemController.add(user1Id, lawnMower);
		ItemDto item3OutDto = itemController.add(user4Id, bike);

		List<ItemDto> listItems = itemController.getUserItem(user1Id);
		List<ItemDto> list2Items = itemController.getUserItem(user4Id);

		assertThat(listItems).asList().hasSize(2);

		assertThat(listItems).asList().contains(item1OutDto);
		assertThat(listItems).asList().contains(item2OutDto);

		assertThat(Optional.of(listItems.get(0))).hasValueSatisfying(
				user -> AssertionsForClassTypes.assertThat(user)
						.hasFieldOrPropertyWithValue("available", true));

		assertThat(Optional.of(listItems.get(1))).hasValueSatisfying(
				user -> AssertionsForClassTypes.assertThat(user)
						.hasFieldOrPropertyWithValue("available", false));

		assertThat(list2Items).asList().hasSize(1);
		assertThat(list2Items).asList().contains(item3OutDto);
		assertThat(Optional.of(listItems.get(0))).hasValueSatisfying(
				user -> AssertionsForClassTypes.assertThat(user)
						.hasFieldOrPropertyWithValue("available", true));


	}

	@Test
	public void shouldSearchItemByNameOrDescription() {

		UserDto user1 = userController.saveNewUser(userAlex1);
		UserDto user4 = userController.saveNewUser(userCustomerName4);
		final Long user1Id = user1.getId();
		final Long user4Id = user4.getId();

		ItemDto item1Dto = itemController.add(user1Id, screwDriver);
		ItemDto item2Dto = itemController.add(user1Id, lawnMower);
		ItemDto item3Dto = itemController.add(user4Id, bike);

		// получаем список доступных вещей, содержащих в названии или описании подстроку er без учета регистра
		// проверяем корректность полученных данных - 1 вещь,
		List<ItemDto> listItems = itemController.searchItems(user1.getId(), "Er");

		assertThat(listItems).asList().hasSize(1);

		assertThat(listItems).asList().startsWith(itemController.getItem(user1.getId(), item1Dto.getId()));
		assertThat(listItems).asList().doesNotContain(itemController.getItem(user1.getId(), item2Dto.getId()));

		assertThat(Optional.of(listItems.get(0))).hasValueSatisfying(
				item -> AssertionsForClassTypes.assertThat(item)
						.hasFieldOrPropertyWithValue("name", "screwdriver"));

		// получаем список доступных вещей, содержащих в названии или описании подстроку er без учета регистра
		// проверяем корректность полученных данных - 2 вещи,
		List<ItemDto> list2Items = itemController.searchItems(user4Id, "e");

		assertThat(list2Items).asList().hasSize(2);

		assertThat(list2Items).asList().contains(itemController.getItem(user4.getId(), item3Dto.getId()));

		assertThat(Optional.of(list2Items.get(0)))
				.hasValueSatisfying(item -> AssertionsForClassTypes.assertThat(item)
						.hasFieldOrPropertyWithValue("name", "screwdriver"));
		assertThat(Optional.of(list2Items.get(1)))
				.hasValueSatisfying(item -> AssertionsForClassTypes.assertThat(item)
						.hasFieldOrPropertyWithValue("name", "bike"));

	}

	@Test
	public void shouldCreateBookingAndGetItByIdByOwner() { // добавление бронирования
		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		User owner = UserMapper.mapToUser(userDto);
		User booker = UserMapper.mapToUser(userDto1);
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		Item item = ItemMapper.mapToItem(itemDto, owner, null);
		bookingController.add(bookerId, bookingItem1Future);

		Optional<BookingDtoResponse> bookingOptional =
				Optional.ofNullable(bookingController.getBooking(ownerId, itemDto.getId()));
		assertThat(bookingOptional).hasValueSatisfying(booking -> assertThat(booking)
				.hasFieldOrPropertyWithValue("id", booking.getId())
				.hasFieldOrPropertyWithValue("start",
						LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.hasFieldOrPropertyWithValue("end",
						LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.hasFieldOrPropertyWithValue("booker", booker)
				.hasFieldOrPropertyWithValue("item", item)
				.hasFieldOrPropertyWithValue("status", BookingStatus.WAITING));

	}

	@Test
	public void shouldCreateBookingAndGetItByIdByBooker() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		User owner = UserMapper.mapToUser(userDto);
		User booker = UserMapper.mapToUser(userDto1);
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		Item item = ItemMapper.mapToItem(itemDto, owner, null);
		bookingController.add(bookerId, bookingItem1Future);

		Optional<BookingDtoResponse> bookingOptional =
				Optional.ofNullable(bookingController.getBooking(bookerId, itemDto.getId()));
		assertThat(bookingOptional).hasValueSatisfying(booking -> assertThat(booking)
				.hasFieldOrPropertyWithValue("id", booking.getId())
				.hasFieldOrPropertyWithValue("start",
						LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.hasFieldOrPropertyWithValue("end",
						LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.hasFieldOrPropertyWithValue("booker", booker)
				.hasFieldOrPropertyWithValue("item", item)
				.hasFieldOrPropertyWithValue("status", BookingStatus.WAITING));

	}


	@Test
	public void shouldFailCreateBookingWithUnavailableItemStatus() {
		userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto1.getId();
		Long bookerId = userDto1.getId();
		itemController.add(ownerId, screwDriver);
		itemController.add(ownerId, lawnMower);


		assertThrows(Exception.class,
				() -> bookingController.add(bookerId, bookingItem2),
				"Не выброшено исключение Exception");

	}

	@Test
	public void shouldFailCreateBookingIfUserIsOwner() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		Long ownerId = userDto.getId();
		itemController.add(ownerId, screwDriver);

		assertThrows(ForbiddenException.class,
				() -> bookingController.add(ownerId, bookingItem1Future3),
				"Не выброшено исключение ForbiddenException");

	}


	@Test
	public void shouldFailCreateBookingWithNonExistingItem() {

		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long bookerId = userDto1.getId();
		assertThrows(NotFoundException.class,
				() -> bookingController.add(bookerId, bookingItem1Future),
				"Не выброшено исключение NotFoundException");

	}

	@Test
	public void shouldFailCreateBookingWithNonExistingUser() {

		assertThrows(NotFoundException.class,
				() -> bookingController.add(nonExistingId, bookingItem1Future3),
				"Не выброшено исключение NotFoundException");

	}


	@Test
	public void shouldFailCreateBookingAndGetItByIdByUserWithoutAccess() { // добавление бронирования
		UserDto userDto1 = userController.saveNewUser(userAlex1);
		UserDto userDto2 = userController.saveNewUser(userCustomerName4);
		UserDto userDto3 = userController.saveNewUser(userAnna5);
		Long ownerId = userDto1.getId();
		Long bookerId = userDto2.getId();
		Long userWithoutAccessId = userDto3.getId();
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		bookingController.add(bookerId, bookingItem1Future);

		assertThrows(ForbiddenException.class,
				() -> bookingController.getBooking(userWithoutAccessId, itemDto.getId()),
				"Не выброшено исключение ForbiddenException.");

	}

	@Test
	public void shouldSetApprovedStatusOfBooking() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		User owner = UserMapper.mapToUser(userDto);
		User booker = UserMapper.mapToUser(userDto1);
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		Item item = ItemMapper.mapToItem(itemDto, owner, null);
		BookingDtoResponse bookingFirst = bookingController.add(bookerId, bookingItem1Future);
		Long bookingId = bookingFirst.getId();
		bookingController.approve(ownerId, bookingId, true);


		Optional<BookingDtoResponse> bookingOptional =
				Optional.ofNullable(bookingController.getBooking(ownerId, itemDto.getId()));
		assertThat(bookingOptional).hasValueSatisfying(booking -> assertThat(booking)
				.hasFieldOrPropertyWithValue("id", booking.getId())
				.hasFieldOrPropertyWithValue("start",
						LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.hasFieldOrPropertyWithValue("end",
						LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.hasFieldOrPropertyWithValue("booker", booker)
				.hasFieldOrPropertyWithValue("item", item)
				.hasFieldOrPropertyWithValue("status", BookingStatus.WAITING));

	}

	@Test
	public void shouldSetRejectedStatusOfBooking() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		User owner = UserMapper.mapToUser(userDto);
		User booker = UserMapper.mapToUser(userDto1);
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		Item item = ItemMapper.mapToItem(itemDto, owner, null);
		BookingDtoResponse bookingFirst = bookingController.add(bookerId, bookingItem1Future);
		Long bookingId = bookingFirst.getId();
		bookingController.approve(ownerId, bookingId, false);


		Optional<BookingDtoResponse> bookingOptional =
				Optional.ofNullable(bookingController.getBooking(ownerId, itemDto.getId()));
		assertThat(bookingOptional).hasValueSatisfying(booking -> assertThat(booking)
				.hasFieldOrPropertyWithValue("id", booking.getId())
				.hasFieldOrPropertyWithValue("start",
						LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.hasFieldOrPropertyWithValue("end",
						LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.hasFieldOrPropertyWithValue("booker", booker)
				.hasFieldOrPropertyWithValue("item", item)
				.hasFieldOrPropertyWithValue("status", BookingStatus.WAITING));

	}

	@Test
	public void shouldFailChangeRejectedStatusOfBooking() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		User owner = UserMapper.mapToUser(userDto);
		User booker = UserMapper.mapToUser(userDto1);
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		Item item = ItemMapper.mapToItem(itemDto, owner, null);
		BookingDtoResponse bookingFirst = bookingController.add(bookerId, bookingItem1Future);
		Long bookingId = bookingFirst.getId();
		bookingController.approve(ownerId, bookingId, false);

		Optional<BookingDtoResponse> bookingOptional =
				Optional.ofNullable(bookingController.getBooking(ownerId, itemDto.getId()));
		assertThat(bookingOptional).hasValueSatisfying(booking -> assertThat(booking)
				.hasFieldOrPropertyWithValue("id", booking.getId())
				.hasFieldOrPropertyWithValue("start",
						LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.hasFieldOrPropertyWithValue("end",
						LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.hasFieldOrPropertyWithValue("booker", booker)
				.hasFieldOrPropertyWithValue("item", item)
				.hasFieldOrPropertyWithValue("status", BookingStatus.WAITING));

	}

	@Test
	public void shouldFailChangeApprovedStatusOfBooking() {
		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		User owner = UserMapper.mapToUser(userDto);
		User booker = UserMapper.mapToUser(userDto1);
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		Item item = ItemMapper.mapToItem(itemDto, owner, null);
		BookingDtoResponse bookingFirst = bookingController.add(bookerId, bookingItem1Future);
		Long bookingId = bookingFirst.getId();
		bookingController.approve(ownerId, bookingId, true);


		Optional<BookingDtoResponse> bookingOptional =
				Optional.ofNullable(bookingController.getBooking(ownerId, itemDto.getId()));
		assertThat(bookingOptional).hasValueSatisfying(booking -> assertThat(booking)
				.hasFieldOrPropertyWithValue("id", booking.getId())
				.hasFieldOrPropertyWithValue("start",
						LocalDateTime.of(2030, 1, 2, 20, 11, 11))
				.hasFieldOrPropertyWithValue("end",
						LocalDateTime.of(2030, 2, 1, 1, 1, 1))
				.hasFieldOrPropertyWithValue("booker", booker)
				.hasFieldOrPropertyWithValue("item", item)
				.hasFieldOrPropertyWithValue("status", BookingStatus.WAITING));
	}

	@Test
	public void shouldFailGetListOfAllBookingByNonExistingUserAsOwner() {

		assertThrows(NotFoundException.class,
				() -> bookingController.getOwnerBookings(nonExistingId, "APPROVED"),
				"Не выброшено исключение NotFoundException.");
	}

	@Test
	public void shouldFailGetListOfAllBookingByOwnerWithUnsupportedStatus() {
		UserDto userDto1 = userController.saveNewUser(userAlex1);
		UserDto userDto2 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto1.getId();
		Long bookerId = userDto2.getId();
		itemController.add(ownerId, screwDriver);
		bookingController.add(bookerId, bookingItem1Future);
		itemController.add(ownerId, adultBike);
		BookingDtoResponse bookingSecondFutureItem1 = bookingController.add(bookerId, bookingItem1Future2);
		BookingDtoResponse bookingThirdFutureItem1 = bookingController.add(bookerId, bookingItem1Future3);
		BookingDtoResponse bookingCurrentItem2 = bookingController.add(bookerId, bookingItem2);

		Long booking2FutureItem1Id = bookingSecondFutureItem1.getId();
		Long booking3FutureItem1Id = bookingThirdFutureItem1.getId();
		Long booking4CurrentItem2Id = bookingCurrentItem2.getId();

		bookingController.approve(ownerId, booking2FutureItem1Id, false);
		bookingController.approve(ownerId, booking3FutureItem1Id, true);
		bookingController.approve(ownerId, booking4CurrentItem2Id, true);

		assertThrows(Exception.class,
				() -> bookingController.getOwnerBookings(ownerId, "NOT_SUPPORTED"),
				"Не выброшено исключение UnsupportedStatusException.");

	}

	@Test
	public void shouldFailGetListOfAllBookingByNonExistingUserAsBooker() {

		assertThrows(NotFoundException.class,
				() -> bookingController.getOwnerBookings(nonExistingId, "APPROVED"),
				"Не выброшено исключение NotFoundException.");

	}

	@Test
	public void shouldFailGetListWithUnsupportedStatusBookingsByBooker() {
		UserDto userDto1 = userController.saveNewUser(userAlex1);
		UserDto userDto2 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto1.getId();
		Long bookerId = userDto2.getId();
		itemController.add(ownerId, screwDriver);
		itemController.add(ownerId, adultBike);
		bookingController.add(bookerId, bookingItem1Future);
		BookingDtoResponse bookingSecondFutureItem1 = bookingController.add(bookerId, bookingItem1Future2);
		BookingDtoResponse bookingThirdFutureItem1 = bookingController.add(bookerId, bookingItem1Future3);
		BookingDtoResponse bookingCurrentItem2 = bookingController.add(bookerId, bookingItem2);

		Long booking2FutureItem1Id = bookingSecondFutureItem1.getId();
		Long booking3FutureItem1Id = bookingThirdFutureItem1.getId();
		Long booking4Item2Id = bookingCurrentItem2.getId();

		bookingController.approve(ownerId, booking2FutureItem1Id, false);
		bookingController.approve(ownerId, booking3FutureItem1Id, true);
		bookingController.approve(ownerId, booking4Item2Id, true);

		assertThrows(Exception.class,
				() -> bookingController.getOwnerBookings(bookerId, "NOT_SUPPORTED"),
				"Не выброшено исключение UnsupportedStatusException.");
	}

	@Test
	public void shouldGetEmptyBookingListByBooker() {

		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long bookerId = userDto1.getId();

		List<BookingDtoResponse> listUsers = bookingController.getBookings(bookerId, "ALL");

		assertThat(listUsers).asList().hasSize(0);
		assertThat(listUsers).asList().isEmpty();

	}

	@Test
	public void shouldFailAddCommentsFromUserWithoutBookings() {

		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long notBookerId = userDto1.getId();
		ItemDto itemDto = itemController.add(ownerId, adultBike);

		assertThrows(Exception.class,
				() -> itemController.addComment(notBookerId, itemDto.getId(), commentToItem1First),
				"Не выброшено исключение Exception.");

	}

	@Test
	public void shouldFailAddCommentsFromOwner() {

		UserDto userDto = userController.saveNewUser(userAlex1);
		Long ownerId = userDto.getId();
		ItemDto itemDto = itemController.add(ownerId, adultBike);

		assertThrows(NotFoundException.class,
				() -> itemController.addComment(ownerId, itemDto.getId(), commentToItem1First),
				"Не выброшено исключение NotFoundException.");

	}


	@Test
	public void shouldFailAddCommentsFromUserWithFutureBookings() {

		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		ItemDto itemDto = itemController.add(ownerId, screwDriver);
		User user = UserMapper.mapToUser(userDto);
		Item item = ItemMapper.mapToItem(itemDto, user, null);
		Long itemId = item.getId();
		bookingController.add(bookerId, bookingItem1Future);
		Long bookingId = userDto.getId();
		bookingController.approve(ownerId, bookingId, true);
		CommentDto commentDto = commentToItem1First;

		assertThrows(Exception.class,
				() -> itemController.addComment(bookerId, itemId, commentDto),
				"Не выброшено исключение Exception.");

	}

	@Test
	public void shouldFailAddCommentsFromUserWithRejectedStatusBookings() {

		UserDto userDto = userController.saveNewUser(userAlex1);
		UserDto userDto1 = userController.saveNewUser(userCustomerName4);
		Long ownerId = userDto.getId();
		Long bookerId = userDto1.getId();
		ItemDto itemDto2 = itemController.add(ownerId, adultBike);
		itemController.add(ownerId, adultBike);
		User user = UserMapper.mapToUser(userDto);
		Item item = ItemMapper.mapToItem(itemDto2, user, null);
		Long itemId = item.getId();
		bookingController.add(bookerId, bookingItem2);
		Long bookingId = userDto.getId();
		bookingController.approve(ownerId, bookingId, false);

		assertThrows(Exception.class,
				() -> itemController.addComment(bookerId, itemId, commentToItem2),
				"Не выброшено исключение Exception.");

	}

}
