package com.laila.pet_symptom_tracker.entities.pet.dto;

import com.laila.pet_symptom_tracker.entities.breed.dto.BreedCompactResponse;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import java.time.LocalDate;

public record PetResponse(
    Long id, String name, LocalDate dateOfBirth, Boolean isAlive, BreedCompactResponse breed) {
  public static PetResponse from(Pet pet) {
    return new PetResponse(
        pet.getId(),
        pet.getName(),
        pet.getDateOfBirth(),
        pet.getIsAlive(),
        BreedCompactResponse.from(pet.getBreed()));
  }
}
