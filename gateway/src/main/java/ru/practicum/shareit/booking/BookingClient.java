package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
	private static final String API_PREFIX = "/bookings";

	@Autowired
	public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
		Map<String, Object> parameters = Map.of(
				"state", state.name(),
				"from", from,
				"size", size
		);
		return get("?state={state}&from={from}&size={size}", userId, parameters);
	}


	public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
		validateBooking(requestDto);
		return post("", userId, requestDto);
	}

	public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
		return get("/" + bookingId, userId);
	}

	public ResponseEntity<Object> approveBooking(long userId, Long bookingId, Boolean approved) {
		return patch("/" + bookingId + "?approved=" + approved, userId);
	}

	public ResponseEntity<Object> getOwnerBookings(long userId, BookingState state) {
		Map<String, Object> parameters = Map.of(
				"state", state.name()
		);
		return get("/owner?state={state}", userId, parameters);
	}


	private void validateBooking(BookItemRequestDto bookingDtoRequest) {
		LocalDateTime start = bookingDtoRequest.getStart();
		LocalDateTime end = bookingDtoRequest.getEnd();
		LocalDateTime now = LocalDateTime.now();
		if (end.isBefore(now)) {
			throw new ValidationException("Время окнчания бронирования не может быть в прошлом, end = "
					+ bookingDtoRequest.getEnd());
		}
		if (start.isBefore(now)) {
			throw new ValidationException("Время начала бронирования не может быть в прошлом, start = "
					+ bookingDtoRequest.getStart());
		}
		if (end.equals(start)) {
			throw new ValidationException("Время окончания бронирования end = " +
					end + " не может быть равно start = " + start);
		}

	}
}
