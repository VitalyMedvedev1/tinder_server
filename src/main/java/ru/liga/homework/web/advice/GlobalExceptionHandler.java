package ru.liga.homework.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.web.response.ErrorResponse;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleException(BusinessLogicException e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> resolveDefaultException(Exception e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}