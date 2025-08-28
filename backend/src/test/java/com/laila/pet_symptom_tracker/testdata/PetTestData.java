package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.pet.dto.PatchPet;
import com.laila.pet_symptom_tracker.entities.pet.dto.PetCompactResponse;
import com.laila.pet_symptom_tracker.entities.pet.dto.PostPet;
import com.laila.pet_symptom_tracker.entities.user.User;
import java.util.List;

public class PetTestData extends TestData {
  public Pet getPet() {
    return DEFAULT_PET;
  }

  public Pet getPet(User owner) {
    return Pet.builder()
        .name(DEFAULT_PET.getName())
        .breed(DEFAULT_BREED)
        .id(VALID_ID)
        .owner(owner)
        .isAlive(true)
        .dateOfBirth(CREATION_DATE)
        .build();
  }

  public PostPet getPostPet() {
    return new PostPet(DEFAULT_PET.getName(), CREATION_DATE, VALID_ID);
  }

  public PetCompactResponse getPetCompactResponse() {
    return new PetCompactResponse(VALID_ID, DEFAULT_PET.getName());
  }

  public PatchPet getPatchPet() {
    return new PatchPet(
        DEFAULT_USER_ID, DEFAULT_PET.getName(), CREATION_DATE, true, null, VALID_ID);
  }

  public List<Pet> getPetList() {
    return List.of(
        Pet.builder().name("Pookie").breed(DEFAULT_BREED).owner(DEFAULT_USER).isAlive(true).build(),
        Pet.builder().name("Cookie").breed(DEFAULT_BREED).owner(DEFAULT_USER).isAlive(true).build(),
        Pet.builder()
            .name("Snookie")
            .breed(DEFAULT_BREED)
            .owner(DEFAULT_USER)
            .isAlive(true)
            .build());
  }

  public List<Pet> getOwnedPetList(User owner) {
    return List.of(
        Pet.builder().name("Pookie").breed(DEFAULT_BREED).owner(owner).isAlive(true).build(),
        Pet.builder().name("Cookie").breed(DEFAULT_BREED).owner(owner).isAlive(true).build());
  }
}
