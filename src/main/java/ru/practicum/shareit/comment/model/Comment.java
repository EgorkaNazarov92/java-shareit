package ru.practicum.shareit.comment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String text;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;

	@CreationTimestamp
	private LocalDateTime created;
}
