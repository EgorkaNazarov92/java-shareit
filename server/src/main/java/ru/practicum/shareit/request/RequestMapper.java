package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestDtoResponse;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
	public static Request mapToRequest(RequestDtoRequest dtoRequest, User user) {
		Request request = new Request();
		request.setDescription(dtoRequest.getDescription());
		request.setUser(user);
		request.setCreated(LocalDateTime.now());
		return request;
	}

	public static RequestDtoResponse mapToRequestDto(Request request) {
		Set<ItemRequestDto> items = new HashSet<>();
		if (!request.getItems().isEmpty()) {
			for (Item item : request.getItems()) {
				ItemRequestDto tmpItem = new ItemRequestDto();
				tmpItem.setId(item.getId());
				tmpItem.setName(item.getName());
				items.add(tmpItem);
			}
		}
		return new RequestDtoResponse(
				request.getId(),
				request.getDescription(),
				request.getCreated(),
				request.getUser(),
				items
		);
	}

	public static List<RequestDtoResponse> mapToRequestDto(Iterable<Request> requests) {
		List<RequestDtoResponse> dtos = new ArrayList<>();
		for (Request request : requests) {
			dtos.add(mapToRequestDto(request));
		}
		return dtos;
	}
}
