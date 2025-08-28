package com.laila.pet_symptom_tracker.exceptions.generic;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}
