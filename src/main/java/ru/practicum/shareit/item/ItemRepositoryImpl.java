package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
	private final List<Item> items = new ArrayList<>();

	@Override
	public Item save(Item item) {
		item.setId(getId());
		items.add(item);
		return item;
	}

	@Override
	public Optional<Item> change(Long itemId, Item newItem) {
		Item item = get(itemId);
		if (!item.getOwner().getId().equals(newItem.getOwner().getId()))
			throw new ValidationException("Пользователь не является владельцем.");
		return items.stream()
				.filter(tmpItem -> itemId.equals(tmpItem.getId()))
				.peek(tmpItem -> {
					if (newItem.getName() != null) tmpItem.setName(newItem.getName());
					if (newItem.getDescription() != null) tmpItem.setDescription(newItem.getDescription());
					if (newItem.getAvailable() != null) tmpItem.setAvailable(newItem.getAvailable());
				}).findFirst();
	}

	@Override
	public Item getItem(Long itemId) {
		Optional<Item> item = items.stream().filter(tmpItem -> tmpItem.getId().equals(itemId)).findFirst();
		if (item.isEmpty()) throw new NotFoundException("Нет такой вещи");
		return item.get();
	}

	@Override
	public List<Item> findByUserId(Long userId) {
		return items.stream()
				.filter(item -> item.getOwner().getId().equals(userId))
				.collect(Collectors.toList());
	}

	@Override
	public List<Item> search(String text) {
		return items.stream()
				.filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
						|| item.getDescription().toLowerCase().contains(text.toLowerCase()))
						&& item.getAvailable())
				.collect(Collectors.toList());
	}

	private Item get(Long itemId) {
		Optional<Item> item = items.stream()
				.filter(tmpItem -> tmpItem.getId().equals(itemId))
				.findFirst();
		if (item.isEmpty()) throw new NotFoundException("Нет такой вещи");
		return item.get();
	}

	private long getId() {
		long lastId = items.stream()
				.mapToLong(Item::getId)
				.max()
				.orElse(0);
		return lastId + 1;
	}
}
