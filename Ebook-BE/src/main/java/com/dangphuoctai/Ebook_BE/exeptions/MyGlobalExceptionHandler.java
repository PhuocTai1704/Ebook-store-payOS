package com.dangphuoctai.Ebook_BE.exeptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.dangphuoctai.Ebook_BE.payloads.APIResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();

        APIResponse res = new APIResponse(message, false);

        return new ResponseEntity<APIResponse>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e) {
        String message = e.getMessage();
        APIResponse res = new APIResponse(message, false);

        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> res = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();

            res.put(fieldName, message);

        });
        return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> myConstraintsVoilationException(ConstraintViolationException e) {
        Map<String, String> res = new HashMap<>();

        e.getConstraintViolations().forEach(voilation -> {
            String fieldName = voilation.getPropertyPath().toString();
            String message = voilation.getMessage();

            res.put(fieldName, message);

        });
        return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<APIResponse> myMissingPathVariableException(MissingPathVariableException e) {
        APIResponse res = new APIResponse(e.getMessage(), false);

        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse> myDataIntegrityException(DataIntegrityViolationException e) {
        APIResponse res = new APIResponse(e.getMessage(), false);

        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleGlobalException(IllegalArgumentException e) {
        APIResponse res = new APIResponse(e.getMessage(), false);
        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<APIResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        APIResponse res = new APIResponse("Method not allowed: " + e.getMethod(), false);
        return new ResponseEntity<APIResponse>(res, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<APIResponse> handleMissingParams(MissingServletRequestParameterException e) {
        String message = "Missing parameter: " + e.getParameterName();
        APIResponse res = new APIResponse(message, false);
        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<APIResponse> handleTypeMismatch(TypeMismatchException e) {
        String message = "Type mismatch: " + e.getPropertyName();
        APIResponse res = new APIResponse(message, false);
        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> handleMessageNotReadable(HttpMessageNotReadableException e) {
        APIResponse res = new APIResponse("Malformed JSON request", false);
        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<APIResponse> handleNotFound(NoHandlerFoundException e) {
        APIResponse res = new APIResponse("API endpoint not found", false);
        return new ResponseEntity<APIResponse>(res, HttpStatus.NOT_FOUND);
    }

}
