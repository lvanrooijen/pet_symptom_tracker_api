package com.laila.pet_symptom_tracker.entities.pet.dto;

import com.laila.pet_symptom_tracker.entities.pet.Pet;

public record PetCompactResponse(Long id, String name) {
  public static PetCompactResponse from(Pet entity) {
    return new PetCompactResponse(entity.getId(), entity.getName());
  }
}
