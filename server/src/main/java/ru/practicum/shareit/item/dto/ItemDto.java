package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
	private Long id;
	private String name;
	private String description;
	@NonNull
	private Boolean available;
	private LocalDateTime lastBooking;
	private LocalDateTime nextBooking;
	private Set<String> comments;
	private Long requestId;
}
