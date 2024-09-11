package ru.practicum.shareit.requests;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.RequestDto;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
	private final RequestClient requestClient;

	@PostMapping
	public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
									  @Valid @RequestBody RequestDto requestDtoRequest) {
		log.info("Creating request {}, userId={}", requestDtoRequest, userId);
		return requestClient.addNewRequest(userId, requestDtoRequest);
	}

	@GetMapping
	public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Get user requests, userId={}", userId);
		return requestClient.getUserRequests(userId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Get all requests, userId={}", userId);
		return requestClient.getAllRequests(userId);
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
											 @PathVariable(name = "requestId") Long requestId) {
		log.info("Get requests, userId={}, requestId={}", userId, requestId);
		return requestClient.getRequest(userId, requestId);
	}

}
