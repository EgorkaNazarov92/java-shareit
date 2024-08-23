package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
	Item save(Item item);

	Optional<Item> change(Long itemId, Item item);

	Item getItem(Long itemId);

	List<Item> findByUserId(Long userId);

	List<Item> search(String text);
}
