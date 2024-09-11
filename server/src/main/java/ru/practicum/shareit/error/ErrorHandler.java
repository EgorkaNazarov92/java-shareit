package ru.practicum.shareit.error;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler({NotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(final Exception e) {
		return new ErrorResponse(e.getMessage(), "Not Found");
	}

	@ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, NullPointerException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleParameterNotValid(final Exception e) {
		return new ErrorResponse(e.getMessage(), "Ошибка валидации");
	}

	@ExceptionHandler({ExistException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleParameterConflict(final Exception e) {
		return new ErrorResponse(e.getMessage(), "Ошибка уникальности");
	}

	@ExceptionHandler({ForbiddenException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleParameterForbidden(final Exception e) {
		return new ErrorResponse(e.getMessage(), "Ошибка прав.");
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(final Exception e) {
		return new ErrorResponse(
				e.getMessage(),
				"Произошла непредвиденная ошибка."
		);
	}
}
