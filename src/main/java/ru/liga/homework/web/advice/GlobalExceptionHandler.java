package ru.liga.homework.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.liga.homework.web.response.ErrorUserResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorUserResponse> handleException(RuntimeException e) {
        ErrorUserResponse response = new ErrorUserResponse("Произошла ошбика: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}