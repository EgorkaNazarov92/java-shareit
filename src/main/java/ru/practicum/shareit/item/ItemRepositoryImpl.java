package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
	private final Map<Long, Item> items = new HashMap<>();

	@Override
	public Item save(Item item) {
		item.setId(getId());
		items.put(item.getId(), item);
		return item;
	}

	@Override
	public Item change(Long itemId, Item newItem) {
		Item item = getItem(itemId);
		if (!item.getOwner().getId().equals(newItem.getOwner().getId()))
			throw new ValidationException("Пользователь не является владельцем.");
		if (newItem.getName() != null) item.setName(newItem.getName());
		if (newItem.getDescription() != null) item.setDescription(newItem.getDescription());
		if (newItem.getAvailable() != null) item.setAvailable(newItem.getAvailable());
		items.put(itemId, item);
		return item;
	}

	@Override
	public Item getItem(Long itemId) {
		Optional<Item> item = Optional.ofNullable(items.get(itemId));
		if (item.isEmpty()) throw new NotFoundException("Нет вещи c id = " + itemId);
		return item.get();
	}

	@Override
	public List<Item> findByUserId(Long userId) {
		return items.values().stream()
				.filter(item -> item.getOwner().getId().equals(userId))
				.collect(Collectors.toList());
	}

	@Override
	public List<Item> search(String text) {
		return items.values().stream()
				.filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
						|| item.getDescription().toLowerCase().contains(text.toLowerCase()))
						&& item.getAvailable())
				.collect(Collectors.toList());
	}

	private long getId() {
		long lastId = items.keySet().stream()
				.max(Long::compareTo)
				.orElse(0L);
		return lastId + 1;
	}
}
