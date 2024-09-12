package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "start_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime start;

	@Column(name = "end_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime end;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "booker_id")
	private User booker;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Booking)) return false;
		return id != null && id.equals(((Booking) o).getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
