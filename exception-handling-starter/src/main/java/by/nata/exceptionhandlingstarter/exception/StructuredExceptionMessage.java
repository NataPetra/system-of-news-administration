package by.nata.exceptionhandlingstarter.exception;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * The {@code StructuredExceptionMessage} record class represents a structured exception message
 * with information about the status code, a map of exception messages, and a timestamp.
 *
 * <p>Usage:</p>
 * <p>Use this record to encapsulate structured error information when an exception occurs in
 * your application. It provides a convenient way to carry relevant details such as the status code,
 * a map of exception messages, and the timestamp when the exception occurred.</p>
 *
 * <p>Example:</p>
 * <pre>
 * Map&lt;String, String&gt; exceptionMessages = Map.of("field1", "Validation error", "field2", "Invalid input");
 * StructuredExceptionMessage exceptionMessage = new StructuredExceptionMessage(400, exceptionMessages, ZonedDateTime.now());
 * </pre>
 *
 * <p>Attributes:</p>
 * <p>- {@code statusCode}: The HTTP status code associated with the error.</p>
 * <p>- {@code exceptionMessages}: A map containing field-specific exception messages.</p>
 * <p>- {@code timeStamp}: The timestamp when the exception occurred, represented as a
 * {@code ZonedDateTime} object.</p>
 *
 * <p>Dependencies:</p>
 * <p>- {@link ZonedDateTime}: A class representing a point in time with a time zone component.</p>
 */
public record StructuredExceptionMessage(int statusCode, Map<String, String> exceptionMessages,
                                         ZonedDateTime timeStamp) {
}
