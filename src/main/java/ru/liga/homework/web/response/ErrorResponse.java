package ru.liga.homework.web.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;


@Setter
@Getter
public class ErrorResponse {

    private static final String LEVEL = "ERROR";
    private String message;
    private String[] stacktrace;
    private static final LocalDateTime localDateTime = LocalDateTime.now();

    public static ErrorResponse build(String message, StackTraceElement[] stacktrace) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        if (stacktrace != null) {
            errorResponse.setStacktrace(Arrays.stream(stacktrace).map(StackTraceElement::toString).toArray(String[]::new));
        }
        return errorResponse;
    }
}