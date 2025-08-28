package com.laila.pet_symptom_tracker.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
  private static final String EMAIL_REGEX =
      "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
  private static final String PASSWORD_REGEX =
      "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])(?!.*\\s).{8,16}$";
  private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
  private static final int MAX_USERNAME_LENGTH = 16;
  private static final int MIN_USERNAME_LENGTH = 3;

  private static final String PASSWORD_REQUIREMENTS =
      "A password must contain a minimum of 8 characters and a maximum of 16 characters, at least 1 number, 1 uppercase letter and 1 special character.";

  public static boolean isValidEmailPattern(String email) {
    if (email == null) return false;
    if (email.isBlank()) return false;
    Matcher matcher = emailPattern.matcher(email);
    return matcher.matches();
  }

  public static boolean isValidPasswordPattern(String password) {
    if (password == null) return false;

    if (password.isBlank()) return false;
    if (password.length() < 8) return false;
    if (password.length() > 16) return false;
    Matcher matcher = passwordPattern.matcher(password.trim());
    return matcher.matches();
  }

  public static boolean isValidUsername(String username) {
    // todo kijk of ik een manier kan vinden om scheldwoorden te filteren
    if (username.isBlank()) return false;
    if (username.length() < MIN_USERNAME_LENGTH) return false;
    if (username.length() > MAX_USERNAME_LENGTH) return false;

    return true;
  }

  public static String getPasswordRequirements() {
    return PASSWORD_REQUIREMENTS;
  }
}
