package com.laila.pet_symptom_tracker.entities.user;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.USER_NOT_REGISTERED;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.authentication.Role;
import com.laila.pet_symptom_tracker.entities.authentication.dto.LoginRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterResponse;
import com.laila.pet_symptom_tracker.entities.blacklistword.ProfanityFilterService;
import com.laila.pet_symptom_tracker.entities.user.dto.UserResponse;
import com.laila.pet_symptom_tracker.exceptions.authentication.InvalidLoginAttemptException;
import com.laila.pet_symptom_tracker.exceptions.authentication.UserNotFoundException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.ProfanityViolationException;
import com.laila.pet_symptom_tracker.securityconfig.JwtService;
import com.laila.pet_symptom_tracker.securityconfig.JwtToken;
import java.awt.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationService authenticationService;
  private final ProfanityFilterService profanityFilterService;

  public RegisterResponse register(RegisterRequest registerRequest) {
    // TODO bijbehorende test scrhijven!
    if (profanityFilterService.violatesProfanityFilter(registerRequest.username())) {
      throw new ProfanityViolationException("Can not create username that contains profanity.");
    }

    // TODO voor morgen. als email al geregistreerd is dan geeft ie internal server. fixen
    User createdUser =
        User.builder()
            .username(registerRequest.username())
            .email(registerRequest.email())
            .password(passwordEncoder.encode(registerRequest.password()))
            .firstName(registerRequest.firstname())
            .lastName(registerRequest.lastname())
            .enabled(true)
            .locked(false)
            .role(Role.USER)
            .build();

    createUser(createdUser);

    String token = jwtService.generateTokenForUser(createdUser);

    return new RegisterResponse(token, UserResponse.from(createdUser));
  }

  public JwtToken login(LoginRequest loginRequest) {
    User user =
        userRepository
            .findByEmailIgnoreCase(loginRequest.username())
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_REGISTERED));

    if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
      return new JwtToken(jwtService.generateTokenForUser(user));
    } else {
      throw new InvalidLoginAttemptException();
    }
  }

  public List<User> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    if (loggedInUser.hasAdminRole()) {
      return userRepository.findAll();
    } else {
      throw new ForbiddenException(ADMIN_ONLY_ACTION);
    }
  }

  public User findByEmail(String email) {
    return userRepository
        .findByEmailIgnoreCase(email)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_REGISTERED));
  }

  @Override
  public void createUser(UserDetails user) {
    if (user instanceof User) {
      userRepository.save((User) user);
    } else {
      throw new IllegalArgumentException(
          "Class: UserService, Method: createUser, Problem: object is not instance of User");
    }
  }

  @Override
  public void updateUser(UserDetails user) {}

  @Override
  public void deleteUser(String username) {}

  @Override
  public void changePassword(String oldPassword, String newPassword) {}

  @Override
  public boolean userExists(String email) {
    User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
    return user != null;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .findByEmailIgnoreCase(email)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_REGISTERED));
  }
}
