package au.com.belong.phone.management.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_INPUT_REQUIRED_CODE;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle custom validation exceptions
    @ExceptionHandler(ValidInputRequiredException.class)
    public ResponseEntity<Object> handleValidInputException(ValidInputRequiredException ex) {
        logger.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(ex.getStatusCode(), ex.getErrorCode(), ex.getReason(), ex.getMessage());
    }

    // Handle ResponseStatusExceptions (e.g., NOT_FOUND, CONFLICT)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("Resource Not Found error: {}", ex.getMessage());
        return buildErrorResponse(ex.getStatusCode(), ex.getErrorCode(), ex.getReason(), ex.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceConflict(ResourceConflictException ex) {
        logger.warn("Resource Conflict error: {}", ex.getMessage());
        return buildErrorResponse(ex.getStatusCode(), ex.getErrorCode(), ex.getReason(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        /*List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);

        return ResponseEntity.badRequest().body(errorResponse);*/

        ValidInputRequiredException cex = new ValidInputRequiredException(
                VALID_INPUT_REQUIRED_CODE,
                ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        logger.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(cex.getStatusCode(), cex.getErrorCode(), cex.getReason(), cex.getMessage());
    }

    // Fallback for any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<Object> buildErrorResponse(
            HttpStatusCode statusCode, String errorCode,
            String reason, String message) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("errorCode", errorCode);
        errorDetails.put("reason", reason);
        errorDetails.put("message", message);

        return new ResponseEntity<>(errorDetails, statusCode);
    }
}