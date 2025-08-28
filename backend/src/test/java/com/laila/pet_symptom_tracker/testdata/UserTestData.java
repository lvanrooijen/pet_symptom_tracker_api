package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.authentication.dto.LoginRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterRequest;
import com.laila.pet_symptom_tracker.entities.user.User;

public class UserTestData extends TestData {
  public RegisterRequest getRegisterRequest() {
    return new RegisterRequest(
        DEFAULT_USER.getUsername(),
        DEFAULT_USER.getEmail(),
        DEFAULT_USER.getPassword(),
        DEFAULT_USER.getFirstName(),
        DEFAULT_USER.getLastName());
  }

  public LoginRequest getLoginRequest() {
    return new LoginRequest(DEFAULT_USER.getEmail(), DEFAULT_USER.getPassword());
  }

  public LoginRequest getLoginRequest(User user) {
    return new LoginRequest(user.getEmail(), user.getPassword());
  }
}
