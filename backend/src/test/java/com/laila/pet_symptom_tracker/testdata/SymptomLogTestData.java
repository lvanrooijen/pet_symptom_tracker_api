package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.symptomlog.SymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PatchSymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PostSymptomLog;
import com.laila.pet_symptom_tracker.entities.user.User;

public class SymptomLogTestData extends TestData {
  public SymptomLog getSymptomLog() {
    return DEFAULT_SYMPTOM_LOG;
  }

  public SymptomLog getSymptomLog(User owner) {
    Pet pet =
        Pet.builder()
            .breed(DEFAULT_BREED)
            .name(DEFAULT_PET.getName())
            .isAlive(true)
            .owner(owner)
            .build();
    return SymptomLog.builder()
        .reportDate(CREATION_DATE)
        .symptom(DEFAULT_SYMPTOM)
        .details(DEFAULT_SYMPTOM_LOG.getDetails())
        .pet(pet)
        .build();
  }

  public PostSymptomLog getPostSymptomLog() {
    return new PostSymptomLog(
        DEFAULT_SYMPTOM_LOG.getPet().getId(),
        VALID_ID,
        DEFAULT_SYMPTOM_LOG.getDetails(),
        DEFAULT_SYMPTOM_LOG.getReportDate());
  }

  public PostSymptomLog getPostSymptomLogWithInvalidPetId() {
    return new PostSymptomLog(
        INVALID_ID,
        VALID_ID,
        DEFAULT_SYMPTOM_LOG.getDetails(),
        DEFAULT_SYMPTOM_LOG.getReportDate());
  }

  public PostSymptomLog getPostSymptomLogWithInvalidSymptomId() {
    return new PostSymptomLog(
        VALID_ID,
        INVALID_ID,
        DEFAULT_SYMPTOM_LOG.getDetails(),
        DEFAULT_SYMPTOM_LOG.getReportDate());
  }

  public PatchSymptomLog getPatchSymptomLog() {
    return new PatchSymptomLog(DEFAULT_SYMPTOM_LOG.getDetails(), VALID_ID, VALID_ID);
  }

  public PatchSymptomLog getPatchSymptomLogWithCustomPetOwner(User petOwner) {
    return new PatchSymptomLog(
        DEFAULT_SYMPTOM_LOG.getDetails(), DEFAULT_SYMPTOM_LOG.getId(), VALID_ID);
  }
}
