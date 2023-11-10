package by.nata.exceptionhandlingstarter.exception;

/**
 * The {@code BadRequestException} class is a custom exception that extends the
 * {@code RuntimeException} class. It is typically used to represent situations
 * where a client request is malformed or invalid.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a {@code BadRequestException} with the specified error message.
     *
     * @param message The error message describing the bad request condition.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
