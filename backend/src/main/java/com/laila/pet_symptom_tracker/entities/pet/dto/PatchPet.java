package com.laila.pet_symptom_tracker.entities.pet.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PatchPet(
    UUID userId,
    String name,
    LocalDate dateOfBirth,
    Boolean isAlive,
    LocalDate dateOfDeath,
    Long breedId) {}
