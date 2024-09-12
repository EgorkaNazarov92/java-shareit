package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
	public static Booking mapToBooking(BookingDtoRequest bookingDtoRequest, User user, Item item) {
		Booking booking = Booking.builder().build();
		booking.setId(bookingDtoRequest.getId());
		booking.setItem(item);
		booking.setStart(bookingDtoRequest.getStart());
		booking.setEnd(bookingDtoRequest.getEnd());
		booking.setStatus(bookingDtoRequest.getStatus());
		booking.setBooker(user);
		return booking;
	}

	public static BookingDtoResponse mapToBookingDto(Booking booking) {
		return new BookingDtoResponse(
				booking.getId(),
				booking.getStart(),
				booking.getEnd(),
				booking.getStatus(),
				booking.getItem(),
				booking.getBooker()
		);
	}

	public static List<BookingDtoResponse> mapToBookingDto(Iterable<Booking> bookings) {
		List<BookingDtoResponse> dtos = new ArrayList<>();
		for (Booking booking : bookings) {
			dtos.add(mapToBookingDto(booking));
		}
		return dtos;
	}
}
