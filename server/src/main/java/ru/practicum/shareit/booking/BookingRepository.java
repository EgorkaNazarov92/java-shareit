package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByBookerId(Long bookerId, Sort sort);

	List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

	List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

	List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

	List<Booking> findByBooker_IdAndStartLessThanEqualAndEndGreaterThanEqual(
			Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort
	);

	List<Booking> findByItemUserId(Long userId, Sort sort);

	List<Booking> findByItemUserIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

	List<Booking> findByItemUserIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

	List<Booking> findByItemUserIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

	List<Booking> findByItemUserIdAndStartLessThanEqualAndEndGreaterThanEqual(
			Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort
	);

	Optional<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);
}
