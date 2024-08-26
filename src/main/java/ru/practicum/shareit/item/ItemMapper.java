package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
	public static Item mapToItem(ItemDto itemDto, User user) {
		Item item = new Item();
		item.setId(itemDto.getId());
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setAvailable(itemDto.getAvailable());
		item.setOwner(user);
		return item;
	}

	public static ItemDto mapToItemDto(Item item) {
		return new ItemDto(
				item.getId(),
				item.getName(),
				item.getDescription(),
				item.getAvailable()
		);
	}

	public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
		List<ItemDto> dtos = new ArrayList<>();
		for (Item item : items) {
			dtos.add(mapToItemDto(item));
		}
		return dtos;
	}
}
