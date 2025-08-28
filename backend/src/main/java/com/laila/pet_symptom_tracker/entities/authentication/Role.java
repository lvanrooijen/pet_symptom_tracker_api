package com.laila.pet_symptom_tracker.entities.authentication;

public enum Role {
  ADMIN,
  MODERATOR,
  USER;

  @Override
  public String toString() {
    return "ROLE_" + super.toString();
  }
}
