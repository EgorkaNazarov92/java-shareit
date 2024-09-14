package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.ForbiddenException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public BookingDtoResponse addNewBooking(Long userId, BookingDtoRequest bookingDtoRequest) {
		User user = getUser(userId);
		Item item = getItem(bookingDtoRequest.getItemId());
		if (item.getUser().getId().equals(userId)) throw new ForbiddenException("Нельзя забронить свою вещь.");
		bookingDtoRequest.setStatus(BookingStatus.WAITING);
		Booking booking = bookingRepository.save(BookingMapper.mapToBooking(bookingDtoRequest, user, item));
		return BookingMapper.mapToBookingDto(booking);
	}

	@Override
	public BookingDtoResponse approveBooking(Long userId, Long bookingId, Boolean approve) {
		Booking booking = getBooking(bookingId);
		Item item = booking.getItem();
		if (!item.getUser().getId().equals(userId))
			throw new ForbiddenException("Только владелец вещи может апрувить запрос");
		BookingStatus status = (approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);
		booking.setStatus(status);
		return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
	}

	@Override
	public BookingDtoResponse getBooking(Long userId, Long bookingId) {
		Booking booking = getBooking(bookingId);
		Item item = booking.getItem();
		if (!Arrays.asList(new Long[]{item.getUser().getId(), booking.getBooker().getId()}).contains(userId))
			throw new ForbiddenException("Только владелец вещи или автор бронирования, смотреть запрос");
		return BookingMapper.mapToBookingDto(booking);
	}

	@Override
	public List<BookingDtoResponse> getBookings(Long userId, String state) {
		getUser(userId);
		Sort sort = Sort.by(Sort.Direction.ASC, "start");
		if (state == null || state.isEmpty()) state = "ALL";
		LocalDateTime now = LocalDateTime.now();
		switch (state) {
			case "ALL":
				return BookingMapper.mapToBookingDto(bookingRepository.findByBookerId(userId, sort));
			case "CURRENT":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByBooker_IdAndStartLessThanEqualAndEndGreaterThanEqual(
								userId, now, now, sort
						)
				);
			case "PAST":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByBooker_IdAndEndIsBefore(userId, now, sort)
				);
			case "FUTURE":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByBooker_IdAndStartIsAfter(userId, now, sort)
				);
			case "WAITING":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING, sort)
				);
			case "REJECTED":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, sort)
				);
			default:
				throw new ValidationException("Нет такого фильтра, state = " + state);
		}
	}

	@Override
	public List<BookingDtoResponse> getOwnerBookings(Long userId, String state) {
		getUser(userId);
		List<Item> items = itemRepository.findByUserId(userId);
		if (items.isEmpty()) throw new NotFoundException("У пользователя с id = " + userId + "нет вещей");
		Sort sort = Sort.by(Sort.Direction.ASC, "start");
		if (state == null || state.isEmpty()) state = "ALL";
		LocalDateTime now = LocalDateTime.now();
		switch (state) {
			case "ALL":
				return BookingMapper.mapToBookingDto(bookingRepository.findByItemUserId(userId, sort));
			case "CURRENT":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByItemUserIdAndStartLessThanEqualAndEndGreaterThanEqual(
								userId, now, now, sort
						)
				);
			case "PAST":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByItemUserIdAndEndIsBefore(userId, now, sort)
				);
			case "FUTURE":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByItemUserIdAndStartIsAfter(userId, now, sort)
				);
			case "WAITING":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByItemUserIdAndStatus(userId, BookingStatus.WAITING, sort)
				);
			case "REJECTED":
				return BookingMapper.mapToBookingDto(
						bookingRepository.findByItemUserIdAndStatus(userId, BookingStatus.REJECTED, sort)
				);
			default:
				throw new ValidationException("Нет такого фильтра, state = " + state);
		}
	}

	private User getUser(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) throw new NotFoundException("Пользователя с id = " + userId.toString() + " не существует");
		return user.get();
	}

	private Item getItem(Long itemId) {
		Optional<Item> item = itemRepository.findById(itemId);
		if (item.isEmpty()) throw new NotFoundException("Вещи с id = " + itemId.toString() + " не существует");
		if (!item.get().getAvailable()) throw new ValidationException("Вещь должна быть доступна.");
		return item.get();
	}

	private Booking getBooking(Long bookingId) {
		Optional<Booking> booking = bookingRepository.findById(bookingId);
		if (booking.isEmpty()) throw new NotFoundException("Резервирования с id = " + bookingId
				+ " не существует");
		return booking.get();
	}
}
