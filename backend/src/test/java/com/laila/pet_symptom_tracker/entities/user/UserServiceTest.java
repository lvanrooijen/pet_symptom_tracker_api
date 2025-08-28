package com.laila.pet_symptom_tracker.entities.user;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.USER_NOT_REGISTERED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.authentication.dto.LoginRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterResponse;
import com.laila.pet_symptom_tracker.exceptions.authentication.InvalidLoginAttemptException;
import com.laila.pet_symptom_tracker.exceptions.authentication.UserNotFoundException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.securityconfig.JwtService;
import com.laila.pet_symptom_tracker.securityconfig.JwtToken;
import com.laila.pet_symptom_tracker.testdata.TestDataProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock UserRepository userRepository;
  @Mock PasswordEncoder passwordEncoder;
  @Mock JwtService jwtService;
  @Mock AuthenticationService authenticationService;
  @InjectMocks UserService userService;

  @Test
  public void register_returns_user() {
    RegisterRequest requestBody = TestDataProvider.USER.getRegisterRequest();

    RegisterResponse result = userService.register(requestBody);

    assertNotNull(result);
    assertEquals(requestBody.email(), result.user().email());
  }

  @Test
  public void login_user_with_invalid_email_should_throw_user_not_found_exception_with_message() {
    LoginRequest loginRequest = TestDataProvider.USER.getLoginRequest();

    when(userRepository.findByEmailIgnoreCase(loginRequest.username()))
        .thenReturn(Optional.empty());

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> userService.login(loginRequest));

    assertEquals(USER_NOT_REGISTERED, exception.getMessage());
  }

  @Test
  public void login_with_invalid_password_should_throw_invalid_login_attempt() {
    LoginRequest loginRequest = TestDataProvider.USER.getLoginRequest();
    User user = TestDataProvider.getUser();

    when(userRepository.findByEmailIgnoreCase(loginRequest.username()))
        .thenReturn(Optional.of(user));
    when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

    assertThrows(InvalidLoginAttemptException.class, () -> userService.login(loginRequest));
  }

  @Test
  public void login_should_return_token() {
    User user = TestDataProvider.getUser();
    LoginRequest loginRequest = TestDataProvider.USER.getLoginRequest(user);

    when(userRepository.findByEmailIgnoreCase(loginRequest.username()))
        .thenReturn(Optional.of(user));
    when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);

    JwtToken result = userService.login(loginRequest);

    assertNotNull(result);
  }

  @Test
  public void get_all_users_as_admin_returns_returns_list_of_all_users() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    userService.getAll();

    verify(userRepository).findAll();
  }

  @Test
  public void get_all_user_as_user_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> userService.getAll());
    assertEquals(ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void
      find_by_email_with_non_existing_email_should_throw_user_not_found_exception_with_message() {
    String invalidEmail = "invalid@email.com";
    when(userRepository.findByEmailIgnoreCase(invalidEmail)).thenReturn(Optional.empty());

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(invalidEmail));

    assertEquals(USER_NOT_REGISTERED, exception.getMessage());
  }

  @Test
  public void find_by_email_should_return_user() {
    User user = TestDataProvider.getUser();
    String email = "valid@email.com";
    when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

    User result = userService.findByEmail(email);
    assertNotNull(result);
  }

  @Test
  public void createUser_should_call_save_method() {
    User user = TestDataProvider.getUser();

    userService.createUser(user);

    verify(userRepository).save(user);
  }

  @Test
  public void userExists_should_return_true_if_user_exists() {
    User user = TestDataProvider.getUser();
    when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

    boolean result = userService.userExists(user.getEmail());

    assertTrue(result);
  }

  @Test
  public void userExists_should_return_false_if_user_does_not_exist() {
    String invalidEmail = "invalid@email.com";

    when(userRepository.findByEmailIgnoreCase(invalidEmail)).thenReturn(Optional.empty());

    boolean result = userService.userExists(invalidEmail);

    assertFalse(result);
  }

  @Test
  public void
      loadByUsername_non_existent_user_should_throw_user_not_found_exception_with_message() {
    String invalidEmail = "INVALID";

    when(userRepository.findByEmailIgnoreCase(invalidEmail)).thenReturn(Optional.empty());

    UserNotFoundException exception =
        assertThrows(
            UserNotFoundException.class, () -> userService.loadUserByUsername(invalidEmail));

    assertEquals(USER_NOT_REGISTERED, exception.getMessage());
  }

  @Test
  public void loadByUsername_should_return_user(){
    User user = TestDataProvider.getUser();

    when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

    UserDetails result = userService.loadUserByUsername(user.getEmail());
    assertNotNull(result);
  }
}
