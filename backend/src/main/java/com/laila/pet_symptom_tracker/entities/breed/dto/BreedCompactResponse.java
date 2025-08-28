package com.laila.pet_symptom_tracker.entities.breed.dto;

import com.laila.pet_symptom_tracker.entities.breed.Breed;

public record BreedCompactResponse(Long id, String name) {
  public static BreedCompactResponse from(Breed entity) {
    return new BreedCompactResponse(entity.getId(), entity.getName());
  }
}
