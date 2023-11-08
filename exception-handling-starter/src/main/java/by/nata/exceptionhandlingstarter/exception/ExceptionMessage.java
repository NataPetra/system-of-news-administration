package by.nata.exceptionhandlingstarter.exception;

import java.time.ZonedDateTime;

public record ExceptionMessage(int statusCode, String message, ZonedDateTime timeStamp) {
}
