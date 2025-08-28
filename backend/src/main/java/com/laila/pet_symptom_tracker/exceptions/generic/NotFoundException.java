package com.laila.pet_symptom_tracker.exceptions.generic;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException() {}
}
