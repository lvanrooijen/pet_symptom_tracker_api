package com.laila.pet_symptom_tracker.entities.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PostPet(
    @NotBlank String name, @NotNull LocalDate dateOfBirth, @NotNull Long breedId) {}
