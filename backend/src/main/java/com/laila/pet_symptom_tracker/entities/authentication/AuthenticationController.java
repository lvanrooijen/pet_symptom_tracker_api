package com.laila.pet_symptom_tracker.entities.authentication;

import com.laila.pet_symptom_tracker.entities.authentication.dto.LoginRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterRequest;
import com.laila.pet_symptom_tracker.entities.authentication.dto.RegisterResponse;
import com.laila.pet_symptom_tracker.entities.user.UserService;
import com.laila.pet_symptom_tracker.mainconfig.ColoredLogger;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import com.laila.pet_symptom_tracker.securityconfig.JwtService;
import com.laila.pet_symptom_tracker.securityconfig.JwtToken;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.BASE_ROUTE)
public class AuthenticationController {
  private final UserService userService;
  private final JwtService jwtService;

  // user registreren
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(
      @RequestBody @Valid RegisterRequest registerRequest) {
    ColoredLogger.prettyInPink("Register request: " + registerRequest);
    RegisterResponse registerResponse = userService.register(registerRequest);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{/id}")
            .buildAndExpand(registerResponse.user().id())
            .toUri();
    return ResponseEntity.created(location).body(registerResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest) {
    ColoredLogger.logInBlue("login attempt");

    JwtToken token = userService.login(loginRequest);

    return ResponseEntity.ok(token);
  }

  // create account als MODERATOR

  // create account als ADMIN

  // user updaten

  // user inloggen

  // user ww veranderen

  // user eigen account verwijderen
}
