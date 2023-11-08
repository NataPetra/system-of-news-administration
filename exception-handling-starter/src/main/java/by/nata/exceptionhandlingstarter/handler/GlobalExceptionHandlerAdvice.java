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
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final String EUROPE_MINSK = "Europe/Minsk";

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleBadRequest(BadRequestException exception) {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handle(EntityNotFoundException exception) {
        return new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionMessage handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        return new ExceptionMessage(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleHttpMessageNotReadableException() {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handlePSQLException(PSQLException exception) {
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getServerErrorMessage().getDetail(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleConnectException(ConnectException exception) {
        return new ExceptionMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StructuredExceptionMessage handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = exception.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
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
