package com.laila.pet_symptom_tracker.entities.disease.dto;

import com.laila.pet_symptom_tracker.entities.disease.Disease;

public record DiseaseCompactResponse(Long id, String name, String description) {
  public static DiseaseCompactResponse from(Disease entity) {
    return new DiseaseCompactResponse(entity.getId(), entity.getName(), entity.getDescription());
  }
}
