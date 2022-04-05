package ru.liga.homework.web.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.web.response.ErrorResponse;

import java.sql.SQLException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> customException(BusinessLogicException e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> defaultException(RuntimeException e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultException(Exception e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage() + e.getCause().getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataBaseHibernateException(DataIntegrityViolationException e) {
        ErrorResponse response = ErrorResponse.build(e.getMessage() + e.getCause().getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}