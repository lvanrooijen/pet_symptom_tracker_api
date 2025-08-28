package com.laila.pet_symptom_tracker.entities.symptom.dto;

import com.laila.pet_symptom_tracker.entities.symptom.Symptom;

public record SymptomResponse(Long id, String name, String description, Boolean isVerified) {
  public static SymptomResponse from(Symptom entity) {
    return new SymptomResponse(
        entity.getId(), entity.getName(), entity.getDescription(), entity.getVerified());
  }
}
