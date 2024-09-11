package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestDtoResponse;

import java.util.List;

public interface RequestService {
	RequestDtoResponse addNewRequest(Long userId, RequestDtoRequest requestDtoRequest);

	List<RequestDtoResponse> getUserRequests(Long userId);

	List<RequestDtoResponse> getAllRequests(Long userId);

	RequestDtoResponse getRequest(Long userId, Long requestId);
}
