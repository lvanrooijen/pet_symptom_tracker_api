package com.laila.pet_symptom_tracker.entities.pettype.dto;

import com.laila.pet_symptom_tracker.entities.pettype.PetType;

public record PetTypeCompactResponse(Long id, String name) {
  public static PetTypeCompactResponse from(PetType entity) {
    return new PetTypeCompactResponse(entity.getId(), entity.getName());
  }
}
