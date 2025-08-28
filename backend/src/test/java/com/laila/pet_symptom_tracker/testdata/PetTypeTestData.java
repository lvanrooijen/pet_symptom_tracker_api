package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PatchPetType;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PetTypeResponse;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PostPetType;
import com.laila.pet_symptom_tracker.entities.user.User;
import java.util.List;

public class PetTypeTestData extends TestData {
  // Pet type
  public PetType getPetType(User creator) {
    return new PetType(VALID_ID, DEFAULT_PET_TYPE.getName(), false, creator);
  }

  public PetType getPetType() {
    return DEFAULT_PET_TYPE;
  }

  public PetTypeResponse getPetTypeResponse() {
    return new PetTypeResponse(
        VALID_ID, DEFAULT_PET_TYPE.getName(), DEFAULT_PET_TYPE.getCreator().getId(), false);
  }

  public PostPetType getPostPetType() {
    return new PostPetType(DEFAULT_PET_TYPE.getName());
  }

  public PatchPetType getPatchPetType() {
    return new PatchPetType(DEFAULT_PET_TYPE.getName());
  }

  public List<PetType> getPetTypeList() {
    return List.of(DEFAULT_PET_TYPE, DEFAULT_PET_TYPE);
  }

  public List<PetType> getAdminPetTypeList() {
    return List.of(
        DEFAULT_PET_TYPE,
        DEFAULT_PET_TYPE,
        new PetType(VALID_ID, DEFAULT_PET_TYPE.getName(), true, DEFAULT_PET_TYPE.getCreator()),
        new PetType(VALID_ID, DEFAULT_PET_TYPE.getName(), true, DEFAULT_PET_TYPE.getCreator()));
  }
}
