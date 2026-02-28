package com.clothing.enterprise.common.handler;

import com.clothing.enterprise.common.exception.BusinessException;
import com.clothing.enterprise.common.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    // Handle Validation Errors (@NotNull, @Email, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request, errors);
    }

    // Fallback for everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request, List.of(ex.getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String errorType = status.getReasonPhrase();
        String finalMessage = "An error occurred while communicating with an external microservice.";
        List<String> details = new ArrayList<>();

        if (ex.contentUTF8() != null && !ex.contentUTF8().isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode downstreamError = mapper.readTree(ex.contentUTF8());

                if (downstreamError.has("message")) {
                    finalMessage = downstreamError.get("message").asText();
                }
                if (downstreamError.has("error")) {
                    errorType = downstreamError.get("error").asText();
                }
            } catch (Exception parseException) {
                details.add("Raw downstream response: " + ex.contentUTF8());
            }
        } else {
            details.add(ex.getMessage());
        }

        // Build YOUR standard ErrorResponse
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errorType,
                finalMessage,
                request.getRequestURI(),
                details.isEmpty() ? null : details
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request, List<String> details) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(response, status);
    }
}