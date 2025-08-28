package com.laila.pet_symptom_tracker.exceptions.generic;

public class DuplicateValueException extends RuntimeException {
  public DuplicateValueException(String message) {
    super(message);
  }
}
