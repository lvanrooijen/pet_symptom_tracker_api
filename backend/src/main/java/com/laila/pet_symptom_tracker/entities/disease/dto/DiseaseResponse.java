package com.laila.pet_symptom_tracker.entities.disease.dto;

import com.laila.pet_symptom_tracker.entities.disease.Disease;
import java.util.UUID;

public record DiseaseResponse(Long id, String name, String description, UUID creatorId) {
  public static DiseaseResponse from(Disease entity) {
    return new DiseaseResponse(
        entity.getId(), entity.getName(), entity.getDescription(), entity.getCreator().getId());
  }
}
