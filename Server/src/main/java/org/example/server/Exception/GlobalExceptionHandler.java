// File: src/main/java/org/example/server/Exception/GlobalExceptionHandler.java

package org.example.server.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Use @RestControllerAdvice instead of @ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors and returns a map of field errors.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles illegal argument exceptions and returns an error message.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentExceptions(
            IllegalArgumentException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions and returns a generic error message.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // Log the exception
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "An unexpected error occurred.");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
