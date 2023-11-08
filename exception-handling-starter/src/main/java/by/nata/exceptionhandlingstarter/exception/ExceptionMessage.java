package by.nata.exceptionhandlingstarter.exception;

import java.time.ZonedDateTime;

/**
 * The {@code ExceptionMessage} record class represents an exception message with
 * information about the status code, error message, and timestamp.
 *
 * <p>Usage:</p>
 * <p>Use this record to encapsulate error information when an exception occurs in
 * your application. It provides a convenient way to carry relevant details such as
 * the status code, error message, and the timestamp when the exception occurred.</p>
 *
 * <p>Example:</p>
 * <pre>
 * ExceptionMessage exceptionMessage = new ExceptionMessage(404, "Resource not found", ZonedDateTime.now());
 * </pre>
 *
 * <p>Attributes:</p>
 * <p>- {@code statusCode}: The HTTP status code associated with the error.</p>
 * <p>- {@code message}: A descriptive error message explaining the exception.</p>
 * <p>- {@code timeStamp}: The timestamp when the exception occurred, represented as
 * a {@code ZonedDateTime} object.</p>
 *
 * <p>Dependencies:</p>
 * <p>- {@link ZonedDateTime}: A class representing a point in time with a time zone
 *   component.</p>
 */
public record ExceptionMessage(int statusCode, String message, ZonedDateTime timeStamp) {
}
