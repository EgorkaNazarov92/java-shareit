package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "requests")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;


	@Column(name = "created_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime created;

	@OneToMany(mappedBy = "request")
	Set<Item> items = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Request)) return false;
		return id != null && id.equals(((Request) o).getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
