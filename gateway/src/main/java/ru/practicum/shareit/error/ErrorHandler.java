package ru.practicum.shareit.error;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, NullPointerException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleParameterNotValid(final Exception e) {
		return new ErrorResponse(e.getMessage(), "Ошибка валидации");
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
