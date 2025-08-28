package com.laila.pet_symptom_tracker.util.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserValidatorTest {
  @Nested
  @DisplayName("Email validation Tests")
  class EmailValidationTests {
    @DisplayName("Returns true for emails with a valid pattern.")
    @ParameterizedTest
    @ValueSource(
        strings = {
          "username@domain.com",
          "username@domain.me",
          "username@domain.co.uk",
          "username@sub.domain.com",
          "username+alias@domain.com"
        })
    void isEmailValid(String email) {
      assertTrue(UserValidator.isValidEmailPattern(email));
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
          "plainUsername",
          "specialCharacterDomain@!.com",
          "email@domain..com",
          "test@domain..com",
          "@missingUsername.com",
          "missingDomain@.com"
        })
    @DisplayName("Returns false for emails with an invalid pattern.")
    void isEmailInvalid(String email) {
      assertFalse(UserValidator.isValidEmailPattern(email));
    }
  }

  @Nested
  @DisplayName("Password Validation tests")
  class PasswordValidationTests {
    // @Disabled("Disabled because spring boot validation is used for this at the moment.")
    @ParameterizedTest
    @ValueSource(strings = {"abcDEF!123", "?@1234abcDEF"})
    @DisplayName("Returns true if password meets requirements.")
    void isValidPassword(String password) {
      assertTrue(UserValidator.isValidPasswordPattern(password));
    }

    // @Disabled("Disabled because spring boot validation is used for this at the moment.")
    @ParameterizedTest
    @ValueSource(
        strings = {
          "a",
          "123456789101112131516abc!!",
          "abcD_E_F_G",
          "Abcdef123456",
          "abcdef123456!",
          "!!!!!!@@",
          "A b c 1 2 3 !"
        })
    @DisplayName("Returns false if password does not meet requirements.")
    void isInvalidPassword(String password) {
      assertFalse(UserValidator.isValidPasswordPattern(password));
    }

    @Test
    @DisplayName("Returns a String that describes the Password requirements.")
    void getPasswordRequirements() {
      String passwordRequirements =
          "A password must contain a minimum of 8 characters and a maximum of 16 characters, at least 1 number, 1 uppercase letter and 1 special character.";
      assertEquals(passwordRequirements, UserValidator.getPasswordRequirements());
    }
  }

  @Nested
  @DisplayName("Username validation Tests")
  class UsernameValidationTests {
    // @Disabled("Disabled because spring boot validation is used for this at the moment.")
    @ParameterizedTest
    @ValueSource(strings = {"Charlie", "?@1234abcDEF"})
    @DisplayName("Returns true if username meets requirements.")
    void isValidUsername(String username) {
      assertTrue(UserValidator.isValidUsername(username));
    }

    // @Disabled("Disabled because spring boot validation is used for this at the moment.")
    @ParameterizedTest
    @ValueSource(strings = {"", "a", "CharlieChapplingBustAGrooveMaybeAMoveDontKnowWho"})
    @DisplayName("Returns false if username does not meet requirements.")
    void isInvalidUsername(String username) {
      assertFalse(UserValidator.isValidUsername(username));
    }
  }
}
