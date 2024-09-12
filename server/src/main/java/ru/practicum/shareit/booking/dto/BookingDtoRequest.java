package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingDtoRequest {
	private Long id;
	@NonNull
	private Long itemId;
	@NonNull
	private LocalDateTime start;
	@NonNull
	private LocalDateTime end;
	private BookingStatus status;
}
