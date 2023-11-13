package by.nata.exceptionhandlingstarter.exception;

/**
 * The {@code AppUserNameExistsException} class is a custom exception that extends the
 * {@code RuntimeException} class. It is typically used to indicate a situation
 * where an attempt is made to create or update a user account with a username
 * that already exists in the application.
 */
public class AppUserNameExistsException extends RuntimeException {

    /**
     * Constructs an {@code AppUserNameExistsException} with the specified error message.
     *
     * @param message The error message describing the existence of the duplicate username.
     */
    public AppUserNameExistsException(String message) {
        super(message);
    }
}
