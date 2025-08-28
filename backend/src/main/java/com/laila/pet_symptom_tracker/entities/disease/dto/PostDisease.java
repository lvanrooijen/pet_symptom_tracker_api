package com.laila.pet_symptom_tracker.entities.disease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostDisease(
    @NotBlank @Size(min = 3, max = 30, message = "Name must be between 3 and 30 charachters")
        String name,
    String description) {}
