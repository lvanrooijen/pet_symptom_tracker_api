package com.laila.pet_symptom_tracker.exceptions.authentication;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
