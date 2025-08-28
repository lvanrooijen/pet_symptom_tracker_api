package com.laila.pet_symptom_tracker.entities.breed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

public record PostBreed(
    @NotBlank @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
        String name,
    @NotNull @NumberFormat(style = NumberFormat.Style.NUMBER) Long petTypeId) {}
