package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository repository;
	private final UserRepository userRepository;

	@Override
	public ItemDto addNewItem(Long userId, ItemDto itemDto) {
		validateItem(itemDto);
		User user = getUser(userId);
		Item item = repository.save(ItemMapper.mapToItem(itemDto, user));
		return ItemMapper.mapToItemDto(item);
	}

	@Override
	public ItemDto changeItem(Long userId, Long itemId, ItemDto itemDto) {
		User user = getUser(userId);
		Optional<Item> item = repository.change(itemId, ItemMapper.mapToItem(itemDto, user));
		return ItemMapper.mapToItemDto(item.get());
	}

	@Override
	public ItemDto getItem(Long userId, Long itemId) {
		User user = getUser(userId);
		return ItemMapper.mapToItemDto(repository.getItem(itemId));
	}

	@Override
	public List<ItemDto> getUserItem(Long userId) {
		User user = getUser(userId);
		return ItemMapper.mapToItemDto(repository.findByUserId(userId));
	}

	@Override
	public List<ItemDto> searchItems(Long userId, String text) {
		User user = getUser(userId);
		if (text == null || text.isEmpty()) return new ArrayList<ItemDto>();
		return ItemMapper.mapToItemDto(repository.search(text));
	}

	private User getUser(Long userId) {
		Optional<User> user = userRepository.get(userId);
		if (user.isEmpty()) throw new NotFoundException("Пользователя с id = " + userId.toString() + " не существует");
		return user.get();
	}

	private void validateItem(ItemDto itemDto) {
		if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
			throw new ValidationException("Имя не может быть пустым и содержать пробелы");
		}
		if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
			throw new ValidationException("Description не может быть пустым и содержать пробелы");
		}
	}
}
