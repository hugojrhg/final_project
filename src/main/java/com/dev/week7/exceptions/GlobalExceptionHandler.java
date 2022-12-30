package com.dev.week7.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public GlobalExceptionHandler() {
        super();
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {

        ApiError apiError = message(HttpStatus.NOT_FOUND, ex);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex, WebRequest request) {

        ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {

        ApiError apiError = message(HttpStatus.NOT_FOUND, ex);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleOrderAlreadyExistsException(OrderAlreadyExistsException ex, WebRequest request) {

        ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);

        apiError.setMessage(String.format("Bad request in %s field, solution: ", ex.getFieldError().getField()) + ex.getFieldError().getDefaultMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }

    private ApiError message(final HttpStatus httpStatus, final Exception ex) {

        final String devMessage = ex.getClass().getSimpleName();
        final String message;

        if (ex.getMessage() == null) {
            message = ex.getClass().getSimpleName();
        } else {
            message = ex.getMessage();
        }

        return new ApiError(httpStatus.value(), message, devMessage);

    }

}
