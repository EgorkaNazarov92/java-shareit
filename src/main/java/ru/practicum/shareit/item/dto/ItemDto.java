package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
	private Long id;
	private String name;
	private String description;
	@NonNull
	private Boolean available;
}
