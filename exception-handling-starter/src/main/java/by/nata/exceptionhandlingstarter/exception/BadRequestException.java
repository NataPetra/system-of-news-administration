package by.nata.exceptionhandlingstarter.exception;

/**
 * The {@code BadRequestException} class is a custom exception that extends the
 * {@code RuntimeException} class. It is typically used to represent situations
 * where a client request is malformed or invalid.
 *
 * <p>Usage:</p>
 * <p>- Instantiate this exception with a descriptive error message when you need
 *   to indicate a bad request condition in your application.</p>
 *
 * <p>Example:</p>
 * <pre>
 * throw new BadRequestException("The request is missing a required parameter.");
 * </pre>
 *
 * <p>Dependencies:</p>
 * <p>- {@link RuntimeException}: The base class for runtime exceptions in Java.</p>
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
