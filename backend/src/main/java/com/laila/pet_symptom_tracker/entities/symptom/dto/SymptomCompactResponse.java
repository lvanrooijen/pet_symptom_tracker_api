package com.laila.pet_symptom_tracker.entities.symptom.dto;

import com.laila.pet_symptom_tracker.entities.symptom.Symptom;

public record SymptomCompactResponse(Long id, String name, String description) {
  public static SymptomCompactResponse from(Symptom entity) {
    return new SymptomCompactResponse(entity.getId(), entity.getName(), entity.getDescription());
  }
}
