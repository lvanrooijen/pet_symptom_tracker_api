package com.laila.pet_symptom_tracker.entities.diseaselog.dto;

import java.time.LocalDate;

public record PatchDiseaseLog(
    Long diseaseId, LocalDate discoveryDate, Boolean isHealed, LocalDate healedOnDate) {}
