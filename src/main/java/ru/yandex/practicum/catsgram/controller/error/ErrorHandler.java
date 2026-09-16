package ru.yandex.practicum.catsgram.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
		return build(NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleDuplicatedData(final DuplicatedDataException e) {
		return build(CONFLICT, e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ErrorResponse handleConditionsNotMet(final ConditionsNotMetException e) {
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleParameterNotValid(final ParameterNotValidException e) {
		return new ErrorResponse("Некорректное значение параметра " +
				e.getParameter() + ": " + e.getReason()
		);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleAnyThrowable(final Throwable e) {
		return new ErrorResponse("Произошла непредвиденная ошибка.");
	}

	private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
		return ResponseEntity
				.status(status)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(message));
	}
}
