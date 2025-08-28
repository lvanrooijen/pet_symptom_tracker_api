package com.laila.pet_symptom_tracker.exceptions.authentication;

import com.laila.pet_symptom_tracker.mainconfig.ColoredLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthenticationExceptionHandler {
  @ExceptionHandler(InvalidLoginAttemptException.class)
  public ResponseEntity<ProblemDetail> invalidLoginAttemptHandler(
      InvalidLoginAttemptException exception) {
    ColoredLogger.logWarning("Failed login attempt: " + exception.getMessage());

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Login failed");
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ProblemDetail> usernameNotFoundHandler(
      UsernameNotFoundException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ProblemDetail> userNotFoundHandler(UserNotFoundException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    return ResponseEntity.badRequest().body(problemDetail);
  }
}
