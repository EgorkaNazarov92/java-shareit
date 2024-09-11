package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
	private final RequestService requestService;

	@PostMapping
	public RequestDtoResponse add(@RequestHeader("X-Sharer-User-Id") Long userId,
								  @RequestBody RequestDtoRequest requestDtoRequest) {
		return requestService.addNewRequest(userId, requestDtoRequest);
	}

	@GetMapping
	public List<RequestDtoResponse> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return requestService.getUserRequests(userId);
	}

	@GetMapping("/all")
	public List<RequestDtoResponse> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return requestService.getAllRequests(userId);
	}

	@GetMapping("/{requestId}")
	public RequestDtoResponse getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @PathVariable(name = "requestId") Long requestId) {
		return requestService.getRequest(userId, requestId);
	}
}
