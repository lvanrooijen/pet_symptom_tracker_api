package com.laila.pet_symptom_tracker.entities.breed.dto;

import com.laila.pet_symptom_tracker.entities.breed.Breed;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PetTypeCompactResponse;
import java.util.UUID;

public record BreedResponse(Long id, String name, PetTypeCompactResponse petType, UUID creatorId) {
  public static BreedResponse from(Breed entity) {
    return new BreedResponse(
        entity.getId(),
        entity.getName(),
        PetTypeCompactResponse.from(entity.getPetType()),
        entity.getCreator().getId());
  }
}
