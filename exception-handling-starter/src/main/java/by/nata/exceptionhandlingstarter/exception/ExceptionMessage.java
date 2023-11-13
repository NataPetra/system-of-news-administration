package by.nata.exceptionhandlingstarter.exception;

import java.time.ZonedDateTime;

/**
 * The {@code ExceptionMessage} record class represents an exception message with
 * information about the status code, error message, and timestamp.
 *
 * <p>Attributes:</p>
 * <p>- {@code statusCode}: The HTTP status code associated with the error.</p>
 * <p>- {@code message}: A descriptive error message explaining the exception.</p>
 * <p>- {@code timeStamp}: The timestamp when the exception occurred.</p>
 */
public record ExceptionMessage(int statusCode, String message, ZonedDateTime timeStamp) {
}
