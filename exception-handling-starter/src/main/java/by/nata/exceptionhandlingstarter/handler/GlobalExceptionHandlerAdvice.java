package by.nata.exceptionhandlingstarter.handler;

import by.nata.exceptionhandlingstarter.exception.BadRequestException;
import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import by.nata.exceptionhandlingstarter.exception.StructuredExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The {@code GlobalExceptionHandlerAdvice} class is a controller advice class that handles
 * exceptions and provides responses for various types of exceptions in your application.
 * It is annotated with {@code @RestControllerAdvice} to indicate that it handles exceptions
 * and returns appropriate HTTP responses.
 *
 * <p>Usage:</p>
 * <p>- Include this class in your Spring application to handle exceptions globally.</p>
 * <p>- It provides exception handling for various exceptions and returns response entities
 *   with appropriate status codes and error messages.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final String EUROPE_MINSK = "Europe/Minsk";

    /**
     * Handles exceptions to type {@code BadRequestException} and returns a response entity with
     * a {@code BAD_REQUEST} status code and an {@code ExceptionMessage} containing the error message.
     *
     * @param exception The {@code BadRequestException} that was thrown.
     * @return An {@code ExceptionMessage} with the status code, error message, and timestamp.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleBadRequest(BadRequestException exception) {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    /**
     * Handles exceptions to type {@code EntityNotFoundException} and returns a response entity with
     * a {@code NOT_FOUND} status code and an {@code ExceptionMessage} containing the error message.
     *
     * @param exception The {@code EntityNotFoundException} that was thrown.
     * @return An {@code ExceptionMessage} with the status code, error message, and timestamp.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handle(EntityNotFoundException exception) {
        return new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    /**
     * Handles exceptions to type {@code HttpRequestMethodNotSupportedException} and returns a response
     * entity with a {@code METHOD_NOT_ALLOWED} status code and an {@code ExceptionMessage} containing
     * the error message.
     *
     * @param exception The {@code HttpRequestMethodNotSupportedException} that was thrown.
     * @return An {@code ExceptionMessage} with the status code, error message, and timestamp.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionMessage handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        return new ExceptionMessage(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    /**
     * Handles exceptions to type {@code HttpMessageNotReadableException} and returns a response
     * entity with a {@code BAD_REQUEST} status code and an {@code ExceptionMessage} indicating a
     * malformed JSON request.
     *
     * @return An {@code ExceptionMessage} with the status code, error message, and timestamp.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleHttpMessageNotReadableException() {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }

    /**
     * Handles exceptions to type {@code PSQLException} and returns a response entity with a
     * {@code BAD_REQUEST} status code and an {@code ExceptionMessage} containing the error message
     * from the database server.
     *
     * @param exception The {@code PSQLException} that was thrown.
     * @return An {@code ExceptionMessage} with the status code, error message, and timestamp.
     */
    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handlePSQLException(PSQLException exception) {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                Objects.requireNonNull(exception.getServerErrorMessage()).getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    /**
     * Handles exceptions to type {@code ConnectException} and returns a response entity with an
     * {@code INTERNAL_SERVER_ERROR} status code and an {@code ExceptionMessage} containing the
     * error message.
     *
     * @param exception The {@code ConnectException} that was thrown.
     * @return An {@code ExceptionMessage} with the status code, error message, and timestamp.
     */
    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleConnectException(ConnectException exception) {
        return new ExceptionMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    /**
     * Handles exceptions to type {@code MethodArgumentNotValidException} and returns a structured
     * response entity with a {@code BAD_REQUEST} status code and an {@code StructuredExceptionMessage}
     * containing field-specific error messages.
     *
     * @param exception The {@code MethodArgumentNotValidException} that was thrown.
     * @return A {@code StructuredExceptionMessage} with the status code, field-specific error messages,
     * and timestamp.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StructuredExceptionMessage handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = exception.getBindingResult().getAllErrors().stream()
                .filter(FieldError.class::isInstance)
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
        return new StructuredExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    /**
     * Handles exceptions to type {@code ConstraintViolationException} and returns a structured
     * response entity with a {@code BAD_REQUEST} status code and an {@code StructuredExceptionMessage}
     * containing field-specific error messages.
     *
     * @param exception The {@code ConstraintViolationException} that was thrown.
     * @return A {@code StructuredExceptionMessage} with the status code, field-specific error messages,
     * and timestamp.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StructuredExceptionMessage handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations()
                .forEach(constraintViolation -> constraintViolation.getPropertyPath()
                        .forEach(error -> errors.put(constraintViolation.getMessage(), error.getName())));
        return new StructuredExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }
}
