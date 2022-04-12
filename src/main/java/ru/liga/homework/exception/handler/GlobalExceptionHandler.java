package ru.liga.homework.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.exception.CustomIOFileException;
import ru.liga.homework.exception.response.ErrorResponse;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> customException(BusinessLogicException e) {
        log.info(e.getMessage());
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(EntityNotFoundException e) {
        log.info(e.getMessage());
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomIOFileException.class)
    public ResponseEntity<ErrorResponse> notFoundException(CustomIOFileException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> defaultException(RuntimeException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.build(e.getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultException(Exception e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.build(e.getMessage() + e.getCause().getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataBaseHibernateException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.build(e.getMessage() + e.getCause().getMessage(), e.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}