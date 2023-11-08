package by.nata.exceptionhandlingstarter.exception;

import java.time.ZonedDateTime;
import java.util.Map;

public record StructuredExceptionMessage(int statusCode, Map<String, String> exceptionMessages,
                                         ZonedDateTime timeStamp) {
}
