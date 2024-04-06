package com.matheusrangel.jwtcodechallenge.exception;

import com.matheusrangel.jwtcodechallenge.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage(), ex);

        var errorDetail = new ErrorResponse.ErrorDetail();
        errorDetail.setErrorCode("INTERNAL_SERVER_ERROR");
        errorDetail.setMessage("Erro Inesperado");

        ErrorResponse errorResponse = buildErrorResponse(
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(errorDetail)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);

        var errorDetails = new ArrayList<ErrorResponse.ErrorDetail>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            var errorDetail = buildErrorDetail(fieldError.getDefaultMessage(), fieldError.getField());
            errorDetails.add(errorDetail);
        }

        var errorResponse = buildErrorResponse(request, HttpStatus.BAD_REQUEST, errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handle(HttpServletRequest request, MissingRequestHeaderException ex) {
        log.error(ex.getMessage(), ex);

        var errorDetail = buildErrorDetail(ex.getMessage(), ex.getHeaderName());
        var errorResponse = buildErrorResponse(request, HttpStatus.BAD_REQUEST, Collections.singletonList(errorDetail));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handle(HttpServletRequest request, NoResourceFoundException ex) {
        log.error(ex.getMessage(), ex);

        var errorDetail = buildErrorDetail(ex.getMessage(), ex.getResourcePath());
        var errorResponse = buildErrorResponse(request, HttpStatus.NOT_FOUND, Collections.singletonList(errorDetail));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private ErrorResponse.ErrorDetail buildErrorDetail(String message, String field) {
        var error = new ErrorResponse.ErrorDetail();
        error.setMessage(message);
        error.setFields(List.of(field));

        return error;
    }

    private ErrorResponse buildErrorResponse(
            HttpServletRequest request,
            HttpStatus httpStatus,
            List<ErrorResponse.ErrorDetail> errors) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .path(request.getServletPath())
                .trace(MDC.get("traceId"))
                .span(MDC.get("spanId"))
                .errors(errors)
                .build();

    }
}
