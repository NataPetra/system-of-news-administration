package by.nata.exceptionhandlingstarter.exception;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * The {@code StructuredExceptionMessage} record class represents a structured exception message
 * with information about the status code, a map of exception messages, and a timestamp.
 *
 * <p>Attributes:</p>
 * <p>- {@code statusCode}: The HTTP status code associated with the error.</p>
 * <p>- {@code exceptionMessages}: A map containing field-specific exception messages.</p>
 * <p>- {@code timeStamp}: The timestamp when the exception occurred, represented as a
 * {@code ZonedDateTime} object.</p>
 */
public record StructuredExceptionMessage(int statusCode, Map<String, String> exceptionMessages,
                                         ZonedDateTime timeStamp) {
}
