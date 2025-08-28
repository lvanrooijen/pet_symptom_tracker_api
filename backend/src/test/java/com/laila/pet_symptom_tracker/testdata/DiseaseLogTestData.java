package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.diseaselog.DiseaseLog;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.DiseaseLogResponse;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.PatchDiseaseLog;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.PostDiseaseLog;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.user.User;
import java.util.List;

public class DiseaseLogTestData extends TestData {
  public DiseaseLog getDiseaseLog() {
    return DEFAULT_DISEASE_LOG;
  }

  public DiseaseLog getDiseaseLog(User owner) {
    Pet pet =
        Pet.builder()
            .name(DEFAULT_PET.getName())
            .owner(owner)
            .breed(DEFAULT_BREED)
            .isAlive(DEFAULT_PET.getIsAlive())
            .dateOfBirth(DEFAULT_PET.getDateOfBirth())
            .build();
    return DiseaseLog.builder().pet(pet).disease(DEFAULT_DISEASE).build();
  }

  public DiseaseLogResponse getDiseaseLogResponse() {
    return new DiseaseLogResponse(
        VALID_ID, DEFAULT_DISEASE_COMPACT, DEFAULT_PET_COMPACT, CREATION_DATE, false, null);
  }

  public PostDiseaseLog getPostDiseaseLog() {
    return new PostDiseaseLog(VALID_ID, VALID_ID, CREATION_DATE);
  }

  public PatchDiseaseLog getPatchDiseaseLog() {
    return new PatchDiseaseLog(VALID_ID, CREATION_DATE, true, HEAL_DATE);
  }

  public List<DiseaseLog> diseaseLogListWithOneOwner(User owner) {
    Pet pet =
        Pet.builder()
            .id(VALID_ID)
            .dateOfBirth(DEFAULT_PET.getDateOfBirth())
            .owner(owner)
            .name(DEFAULT_PET.getName())
            .build();

    return List.of(
        DiseaseLog.builder()
            .pet(pet)
            .disease(DEFAULT_DISEASE)
            .isHealed(true)
            .discoveryDate(CREATION_DATE)
            .healedOnDate(HEAL_DATE)
            .build(),
        DiseaseLog.builder()
            .pet(DEFAULT_PET)
            .disease(DEFAULT_DISEASE)
            .isHealed(false)
            .discoveryDate(CREATION_DATE)
            .build(),
        DiseaseLog.builder()
            .pet(DEFAULT_PET)
            .disease(DEFAULT_DISEASE)
            .isHealed(false)
            .discoveryDate(CREATION_DATE)
            .build());
  }
}
