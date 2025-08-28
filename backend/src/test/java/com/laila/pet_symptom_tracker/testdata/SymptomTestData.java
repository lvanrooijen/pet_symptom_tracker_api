package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.symptom.Symptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PatchSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PostSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.SymptomResponse;
import java.util.List;

public class SymptomTestData extends TestData {
  public Symptom getSymptom() {
    return DEFAULT_SYMPTOM;
  }

  public Symptom getSoftDeletedSymptom() {
    Symptom symptom =
        Symptom.builder()
            .name(DEFAULT_SYMPTOM.getName())
            .description(DEFAULT_SYMPTOM.getDescription())
            .verified(false)
            .build();
    symptom.setDeleted(true);
    return symptom;
  }

  public Symptom getUnverifiedSymptom() {
    return Symptom.builder()
        .name(DEFAULT_SYMPTOM.getName())
        .description(DEFAULT_SYMPTOM.getDescription())
        .verified(false)
        .build();
  }

  public SymptomResponse getSymptomResponse() {
    return new SymptomResponse(
        VALID_ID, DEFAULT_SYMPTOM.getName(), DEFAULT_SYMPTOM.getDescription(), true);
  }

  public PostSymptom getPostSymptom() {
    return new PostSymptom(DEFAULT_SYMPTOM.getName(), DEFAULT_SYMPTOM.getDescription());
  }

  public PatchSymptom getPatchSymptom() {
    return new PatchSymptom(DEFAULT_SYMPTOM.getName(), DEFAULT_SYMPTOM.getDescription(), null);
  }

  public PatchSymptom getPatchSymptomWithVerifiedTrue() {
    return new PatchSymptom(DEFAULT_SYMPTOM.getName(), DEFAULT_SYMPTOM.getDescription(), true);
  }
}
