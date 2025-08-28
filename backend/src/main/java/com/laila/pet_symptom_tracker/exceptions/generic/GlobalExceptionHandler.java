package com.laila.pet_symptom_tracker.exceptions.generic;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidationExceptions(
      MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Request body does not meet requirements");

    problemDetail.setProperty("errors", errors);

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ProblemDetail> badRequestHandler(BadRequestException exception) {
    String message;
    if (exception.getMessage() == null || exception.getMessage().isBlank()) {
      message = "Bad Request";
    } else {
      message = exception.getMessage();
    }

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ProblemDetail> forbiddenHandler(ForbiddenException exception) {
    String message;
    if (exception.getMessage() == null || exception.getMessage().isBlank()) {
      message = "Forbidden";
    } else {
      message = exception.getMessage();
    }

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    problemDetail.setTitle("Forbidden");
    problemDetail.setDetail(message);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Void> notFoundHandler(NotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(NoContentException.class)
  public ResponseEntity<Void> noContentHandler() {
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Request body is missing or incorrectly formatted");
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(DuplicateValueException.class)
  public ResponseEntity<ProblemDetail> duplicateValueHandler(DuplicateValueException exception) {
    String msg = exception.getMessage();
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(PatchDeletedEntityException.class)
  public ResponseEntity<ProblemDetail> patchDeletedEntityHandler(
      PatchDeletedEntityException exception) {
    String msg = exception.getMessage();
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(ProfanityViolationException.class)
  public ResponseEntity<ProblemDetail> profanityViolationHandler(
      ProfanityViolationException exception) {
    String msg = exception.getMessage();
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
    return ResponseEntity.badRequest().body(problemDetail);
  }
}
