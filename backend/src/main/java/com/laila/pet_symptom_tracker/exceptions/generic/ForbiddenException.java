package com.laila.pet_symptom_tracker.exceptions.generic;

public class ForbiddenException extends RuntimeException {
  public ForbiddenException(String message) {
    super(message);
  }

  public ForbiddenException() {}
}
