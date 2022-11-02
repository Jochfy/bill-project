package com.example.bill.config;

import com.example.bill.dto.ApiErrorDto;
import static com.example.bill.helpers.LabelConstantHelper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Component allows to handle exceptions across the whole the application
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler {

    public static final String CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE = "A ConstraintViolationException occurred : ";
    public static final String HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION_MESSAGE = " media type is not supported. Supported media types are ";

    public static final String EXCEPTION_MESSAGE = "An Exception Error occurred :";



    /**
     * Exceptions that can be thrown during Constraint Violation operation.
     * @param ex - The Constraint Violation exception
     * @return ResponseEntity that contain the HTTP status code and the error message associated with exception and List of constructed error messages
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error(CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE + ex.getConstraintViolations());
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
     * Occurs when the client sends a request with unsupported media type:
     * @param ex Http Media Type Not Supported Exception
     * @return ResponseEntity that contain the HTTP status code and the error message associated with exception and List of constructed error messages
     */
    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION_MESSAGE);
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(COMMA_SEPARATION_SIGN).append(SPACE));
        ApiErrorDto apiError = new ApiErrorDto(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        log.error(builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Catch-all type of logic that deals with all other exceptions that don't have specific handlers
     * @param ex - thrown by the execution of the method or constructor
     * @return ResponseEntity that contain the HTTP status code and the error message associated with exception
     */
    @ExceptionHandler({ Exception.class })
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<Object> handleAll(Exception ex) {
        log.error(EXCEPTION_MESSAGE+ ex);
        ApiErrorDto apiError = new ApiErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), EXCEPTION_MESSAGE);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

}
