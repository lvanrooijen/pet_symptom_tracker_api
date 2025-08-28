package com.laila.pet_symptom_tracker.entities.symptomlog.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PostSymptomLog(
    @NotNull Long petId, @NotNull Long symptomId, String details, @NotNull LocalDate reportDate) {}
