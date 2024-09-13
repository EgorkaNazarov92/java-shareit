package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.error.ForbiddenException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
	private final ItemRepository repository;
	private final UserRepository userRepository;
	private final BookingRepository bookingRepository;
	private final CommentRepository commentRepository;
	private final RequestRepository requestRepository;

	@Override
	@Transactional
	public ItemDto addNewItem(Long userId, ItemDto itemDto) {
		User user = getUser(userId);
		Request request = null;
		if (itemDto.getRequestId() != null) request = getRequest(itemDto.getRequestId());
		Item item = repository.save(ItemMapper.mapToItem(itemDto, user, request));
		return ItemMapper.mapToItemDto(item, userId);
	}

	@Override
	@Transactional
	public ItemDto changeItem(Long userId, Long itemId, ItemDto itemDto) {
		User user = getUser(userId);
		Item item = repository.getById(itemId);
		if (!item.getUser().getId().equals(userId))
			throw new ForbiddenException("Только владелец может изменять вешью");
		if (itemDto.getName() != null) item.setName(itemDto.getName());
		if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
		if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
		return ItemMapper.mapToItemDto(repository.save(item), userId);
	}

	@Override
	public ItemDto getItem(Long userId, Long itemId) {
		getUser(userId);
		Item item = getItem(itemId);
		return ItemMapper.mapToItemDto(item, userId);
	}

	@Override
	public List<ItemDto> getUserItem(Long userId) {
		getUser(userId);
		return ItemMapper.mapToItemDto(repository.findByUserId(userId), userId);
	}

	@Override
	public List<ItemDto> searchItems(Long userId, String text) {
		getUser(userId);
		if (text == null || text.isEmpty()) return new ArrayList<ItemDto>();
		return ItemMapper.mapToItemDto(repository.search(text), userId);
	}

	@Override
	@Transactional
	public CommentDto addNewComment(Long userId, Long itemId, CommentDto commentDto) {
		User user = getUser(userId);
		Item item = getItem(itemId);
		checkBooking(userId, itemId);
		Comment comment = CommentMapper.mapToComment(commentDto, user, item);

		return CommentMapper.mapToCommentDto(commentRepository.save(comment));
	}

	private User getUser(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) throw new NotFoundException("Пользователя с id = " + userId.toString() + " не существует");
		return user.get();
	}

	private Item getItem(Long itemId) {
		Optional<Item> item = repository.findById(itemId);
		if (item.isEmpty()) throw new NotFoundException("Вещи с id = " + itemId + " не существует");
		return item.get();
	}

	private Request getRequest(Long requestId) {
		Optional<Request> request = requestRepository.findById(requestId);
		if (request.isEmpty()) throw new NotFoundException("Запроса с id = " + requestId + " не существует");
		return request.get();
	}

	private void checkBooking(Long userId, Long itemId) {
		Optional<Booking> booking = bookingRepository.findByBookerIdAndItemId(userId, itemId);
		if (booking.isEmpty()) throw new NotFoundException("Пользователя с id = " + userId.toString()
				+ " не брал вещь с id = " + itemId);
		if (!booking.get().getEnd().isBefore(LocalDateTime.now()))
			throw new ValidationException("Пользователь еще не брал вещь");
	}
}
