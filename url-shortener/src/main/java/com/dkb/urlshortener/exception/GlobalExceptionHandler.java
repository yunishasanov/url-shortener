package com.dkb.urlshortener.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UrlNotFoundException.class)
  public ResponseEntity<Object> handleUrlNotFoundException(UrlNotFoundException ex,
      WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("message", ex.getMessage());
    body.put("status", HttpStatus.NOT_FOUND.value());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("message", "An unexpected error occurred");
    body.put("details", ex.getMessage());
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex,
      WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("message", ex.getMessage());
    body.put("status", HttpStatus.BAD_REQUEST.value());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
