package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
	private final BookingService bookingService;

	@PostMapping
	public BookingDtoResponse add(@RequestHeader("X-Sharer-User-Id") Long userId,
								  @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
		return bookingService.addNewBooking(userId, bookingDtoRequest);
	}

	@PatchMapping("/{bookingId}")
	public BookingDtoResponse approve(@RequestHeader("X-Sharer-User-Id") Long userId,
									  @PathVariable(name = "bookingId") Long bookingId,
									  @RequestParam Boolean approved) {
		return bookingService.approveBooking(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public BookingDtoResponse getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @PathVariable(name = "bookingId") Long bookingId) {
		return bookingService.getBooking(userId, bookingId);
	}

	@GetMapping
	public List<BookingDtoResponse> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
												@RequestParam(required = false) String state) {
		return bookingService.getBookings(userId, state);
	}

	@GetMapping("/owner")
	public List<BookingDtoResponse> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
													 @RequestParam(required = false) String state) {
		return bookingService.getOwnerBookings(userId, state);
	}
}
