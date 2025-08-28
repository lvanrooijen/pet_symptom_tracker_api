package com.laila.pet_symptom_tracker.entities.symptomlog.dto;

import com.laila.pet_symptom_tracker.entities.symptom.dto.SymptomCompactResponse;
import com.laila.pet_symptom_tracker.entities.symptomlog.SymptomLog;
import java.time.LocalDate;

public record SymptomLogResponse(
    Long id, SymptomCompactResponse symptom, String details, LocalDate reportDate) {
  public static SymptomLogResponse from(SymptomLog entity) {
    return new SymptomLogResponse(
        entity.getId(),
        SymptomCompactResponse.from(entity.getSymptom()),
        entity.getDetails(),
        entity.getReportDate());
  }
}
