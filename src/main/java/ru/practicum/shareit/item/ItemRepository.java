package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
	Item save(Item item);

	Item change(Long itemId, Item item);

	Item getItem(Long itemId);

	List<Item> findByUserId(Long userId);

	List<Item> search(String text);
}
