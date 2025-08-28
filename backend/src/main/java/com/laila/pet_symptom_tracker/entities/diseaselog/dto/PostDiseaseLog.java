package com.laila.pet_symptom_tracker.entities.diseaselog.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PostDiseaseLog(
    @NotNull Long diseaseId, @NotNull Long petId, @NotNull LocalDate discoveryDate) {}
