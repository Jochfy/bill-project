package com.example.bill.config;

import com.example.bill.dto.ApiErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Componenet allows to handle exceptions across the whole the application
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler {


    /**
     * Exceptions that can be thrown during Constraint Violation operation.
     * @param ex - The Constraint Violation exception
     * @return ResponseEntity that contain the HTTP status code and the error message associated with exception and List of constructed error messages
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error("A ConstraintViolationException occured : " + ex.getConstraintViolations());
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        ApiErrorDto apiError =
                new ApiErrorDto(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Exceptions is thrown when method argument is not the expected type.
     * @param ex - Type Mismatch Exception
     * @return ResponseEntity that contain the HTTP status code and the error message associated with exception and List of constructed error messages
     */
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        log.error("A MethodArgumentTypeMismatchException occured : "+ ex);
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiErrorDto apiError =
                new ApiErrorDto(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Catch-all type of logic that deals with all other exceptions that don't have specific handlers
     * @param ex - thrown by the execution of the method or constructor
     * @return ResponseEntity that contain the HTTP status code and the error message associated with exception
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        log.error("An Exception occured : "+ ex);
        ApiErrorDto apiError = new ApiErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }


}
