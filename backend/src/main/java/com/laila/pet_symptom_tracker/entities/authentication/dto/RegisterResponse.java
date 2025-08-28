package com.laila.pet_symptom_tracker.entities.authentication.dto;

import com.laila.pet_symptom_tracker.entities.user.dto.UserResponse;

public record RegisterResponse(String token, UserResponse user) {}
