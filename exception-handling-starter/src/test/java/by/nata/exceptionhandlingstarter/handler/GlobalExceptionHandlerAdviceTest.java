package by.nata.exceptionhandlingstarter.handler;

import by.nata.exceptionhandlingstarter.exception.BadRequestException;
import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import by.nata.exceptionhandlingstarter.exception.StructuredExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.ConnectException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerAdviceTest {

    private final GlobalExceptionHandlerAdvice globalExceptionHandlerAdvice = new GlobalExceptionHandlerAdvice();

    @Test
    void handleBadRequestException() {
        BadRequestException badRequestException = new BadRequestException("Bad request");
        ExceptionMessage response = globalExceptionHandlerAdvice.handleBadRequest(badRequestException);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.message()).isEqualTo("Bad request");
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException("Entity not found");
        ExceptionMessage response = globalExceptionHandlerAdvice.handle(entityNotFoundException);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.message()).isEqualTo("Entity not found");
    }

    @Test
    void handleMethodNotSupported() {
        String errorMessage = "Method not supported";
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException(errorMessage);
        ExceptionMessage response = globalExceptionHandlerAdvice.handleMethodNotSupported(exception);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
        assertThat(response.message()).isEqualTo("Request method 'Method not supported' is not supported");
    }

    @Test
    void handleHttpMessageNotReadableException() {
        ExceptionMessage response = globalExceptionHandlerAdvice.handleHttpMessageNotReadableException();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.message()).isEqualTo("Malformed JSON request");
    }

    @Test
    void handleConnectException() {
        String errorMessage = "Connection failed";
        ConnectException connectException = new ConnectException(errorMessage);
        ExceptionMessage response = globalExceptionHandlerAdvice.handleConnectException(connectException);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.message()).isEqualTo(errorMessage);
    }

    @Test
    void handleMethodArgumentNotValid() {
        MethodParameter methodParameter = mock(MethodParameter.class);
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        StructuredExceptionMessage response = globalExceptionHandlerAdvice.handleMethodArgumentNotValid(exception);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void handleConstraintViolation() {
        ConstraintViolationException exception = createConstraintViolationException();

        StructuredExceptionMessage response = globalExceptionHandlerAdvice.handleConstraintViolation(exception);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ConstraintViolationException createConstraintViolationException() {
        Set<ConstraintViolation<?>> constraintViolations = createConstraintViolations();

        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        when(exception.getConstraintViolations()).thenReturn(constraintViolations);

        return exception;
    }

    private Set<ConstraintViolation<?>> createConstraintViolations() {
        ConstraintViolation<?> constraintViolation1 = createConstraintViolation("Error message 1", "field1");
        ConstraintViolation<?> constraintViolation2 = createConstraintViolation("Error message 2", "field2");

        return Set.of(constraintViolation1, constraintViolation2);
    }

    private ConstraintViolation<?> createConstraintViolation(String message, String propertyPath) {
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getMessage()).thenReturn(message);

        Path path = mock(Path.class);
        when(path.toString()).thenReturn(propertyPath);

        when(constraintViolation.getPropertyPath()).thenReturn(path);

        return constraintViolation;
    }

}