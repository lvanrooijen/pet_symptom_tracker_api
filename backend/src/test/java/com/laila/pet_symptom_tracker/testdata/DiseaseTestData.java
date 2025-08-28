package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.disease.Disease;
import com.laila.pet_symptom_tracker.entities.disease.dto.DiseaseCompactResponse;
import com.laila.pet_symptom_tracker.entities.disease.dto.DiseaseResponse;
import com.laila.pet_symptom_tracker.entities.disease.dto.PatchDisease;
import com.laila.pet_symptom_tracker.entities.disease.dto.PostDisease;
import com.laila.pet_symptom_tracker.entities.user.User;

public class DiseaseTestData extends TestData {
  public Disease getDisease(User creator) {
    return new Disease(
        VALID_ID, DEFAULT_DISEASE.getName(), DEFAULT_DISEASE.getDescription(), false, creator);
  }

  public Disease getDisease() {
    return DEFAULT_DISEASE;
  }

  public Disease getDeletedDisease(User creator) {
    return new Disease(
        VALID_ID, DEFAULT_DISEASE.getName(), DEFAULT_DISEASE.getDescription(), true, creator);
  }

  public Disease getDeletedDisease() {
    return new Disease(
        VALID_ID,
        DEFAULT_DISEASE.getName(),
        DEFAULT_DISEASE.getDescription(),
        true,
        DEFAULT_DISEASE.getCreator());
  }

  public PostDisease getPostDisease() {
    return new PostDisease(DEFAULT_DISEASE.getName(), DEFAULT_DISEASE.getDescription());
  }

  public PatchDisease getPatchDisease() {
    return new PatchDisease("Test", "new description");
  }

  public DiseaseResponse getDiseaseResponse() {
    return new DiseaseResponse(
        VALID_ID,
        DEFAULT_DISEASE.getName(),
        DEFAULT_DISEASE.getDescription(),
        DEFAULT_DISEASE.getCreator().getId());
  }

  public DiseaseCompactResponse getDiseaseCompactResponse() {
    return DEFAULT_DISEASE_COMPACT;
  }
}
