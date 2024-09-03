package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
	BookingDtoResponse addNewBooking(Long userId, BookingDtoRequest booking);

	BookingDtoResponse approveBooking(Long userId, Long bookingId, Boolean approve);

	BookingDtoResponse getBooking(Long userId, Long bookingId);

	List<BookingDtoResponse> getBookings(Long userId, String state);

	List<BookingDtoResponse> getOwnerBookings(Long userId, String state);
}
