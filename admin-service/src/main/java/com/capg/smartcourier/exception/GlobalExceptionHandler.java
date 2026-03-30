package com.capg.smartcourier.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Custom exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔥 Feign Exception (IMPORTANT)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {

        String message = "Downstream service error";

        if (ex.status() == 404) {
            message = "Requested resource not found in another service";
        } else if (ex.status() == 500) {
            message = "Internal error in another service";
        }

        return new ResponseEntity<>(
                new ErrorResponse(message, ex.status()),
                HttpStatus.valueOf(ex.status())
        );
    }

    // 🔹 General exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex) {

        return new ResponseEntity<>(
                new ErrorResponse("Something went wrong: " + ex.getMessage(), 500),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}