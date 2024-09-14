package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private Boolean available;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ElementCollection
	@CollectionTable(name = "bookings", joinColumns = @JoinColumn(name = "item_id"))
	@Column(name = "start_date")
	private Set<LocalDateTime> bookingStartDates = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "bookings", joinColumns = @JoinColumn(name = "item_id"))
	@Column(name = "end_date")
	private Set<LocalDateTime> bookingEndDates = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "comments", joinColumns = @JoinColumn(name = "item_id"))
	@Column(name = "text")
	private Set<String> comments = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "request_id")
	private Request request;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item)) return false;
		return id != null && id.equals(((Item) o).getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
