package com.laila.pet_symptom_tracker.exceptions.authentication;

public class UsernameNotFoundException extends RuntimeException {
  public UsernameNotFoundException(String message) {
    super(message);
  }
}
