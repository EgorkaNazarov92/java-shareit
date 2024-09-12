package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
	public static Item mapToItem(ItemDto itemDto, User user, Request request) {
		Item item = Item.builder().build();
		item.setId(itemDto.getId());
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setAvailable(itemDto.getAvailable());
		item.setUser(user);
		item.setRequest(request);
		return item;
	}

	public static ItemDto mapToItemDto(Item item, Long userId) {
		return new ItemDto(
				item.getId(),
				item.getName(),
				item.getDescription(),
				item.getAvailable(),
				item.getUser().getId().equals(userId) && item.getBookingEndDates() != null ?
						item.getBookingEndDates()
								.stream()
								.filter(localDateTime -> localDateTime.isBefore(LocalDateTime.now()))
								.sorted(Comparator.reverseOrder())
								.findFirst().orElse(null)
						: null,

				item.getUser().getId().equals(userId) && item.getBookingStartDates() != null ?
						item.getBookingStartDates()
								.stream()
								.filter(localDateTime -> localDateTime.isAfter(LocalDateTime.now()))
								.sorted()
								.findFirst().orElse(null)
						: null,
				item.getComments() == null ? new HashSet<String>() : item.getComments(),
				item.getRequest() == null ? null : item.getRequest().getId()
		);
	}

	public static List<ItemDto> mapToItemDto(Iterable<Item> items, Long userId) {
		List<ItemDto> dtos = new ArrayList<>();
		for (Item item : items) {
			dtos.add(mapToItemDto(item, userId));
		}
		return dtos;
	}
}
