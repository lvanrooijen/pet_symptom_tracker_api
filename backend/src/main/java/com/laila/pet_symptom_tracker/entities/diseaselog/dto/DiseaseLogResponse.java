package com.laila.pet_symptom_tracker.entities.diseaselog.dto;

import com.laila.pet_symptom_tracker.entities.disease.dto.DiseaseCompactResponse;
import com.laila.pet_symptom_tracker.entities.diseaselog.DiseaseLog;
import com.laila.pet_symptom_tracker.entities.pet.dto.PetCompactResponse;
import java.time.LocalDate;

public record DiseaseLogResponse(
    Long id,
    DiseaseCompactResponse disease,
    PetCompactResponse pet,
    LocalDate discoveryDate,
    Boolean isHealed,
    LocalDate healedOnDate) {
  public static DiseaseLogResponse from(DiseaseLog entity) {
    return new DiseaseLogResponse(
        entity.getId(),
        DiseaseCompactResponse.from(entity.getDisease()),
        PetCompactResponse.from(entity.getPet()),
        entity.getDiscoveryDate(),
        entity.getIsHealed(),
        entity.getHealedOnDate());
  }
}
