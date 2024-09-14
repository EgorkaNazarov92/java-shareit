package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestDtoResponse;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
	private final RequestRepository requestRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public RequestDtoResponse addNewRequest(Long userId, RequestDtoRequest requestDtoRequest) {
		User user = getUser(userId);
		Request request = RequestMapper.mapToRequest(requestDtoRequest, user);
		return RequestMapper.mapToRequestDto(requestRepository.save(request));
	}

	@Override
	public List<RequestDtoResponse> getUserRequests(Long userId) {
		getUser(userId);
		return RequestMapper.mapToRequestDto(requestRepository.findByUserId(userId));
	}

	@Override
	public List<RequestDtoResponse> getAllRequests(Long userId) {
		getUser(userId);
		return RequestMapper.mapToRequestDto(requestRepository.findAll());
	}

	@Override
	public RequestDtoResponse getRequest(Long userId, Long requestId) {
		getUser(userId);
		return RequestMapper.mapToRequestDto(getRequest(requestId));
	}


	private User getUser(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) throw new NotFoundException("Пользователя с id = " + userId.toString() + " не существует");
		return user.get();
	}

	private Request getRequest(Long requestId) {
		Optional<Request> request = requestRepository.findById(requestId);
		if (request.isEmpty()) throw new NotFoundException("Запроса с id = " + requestId
				+ " не существует");
		return request.get();
	}
}
