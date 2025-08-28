package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.user.User;

public class TestDataProvider extends TestData {
  public static final BlackListWordTestData BLACK_LIST_WORD = new BlackListWordTestData();
  public static final BreedTestData BREED = new BreedTestData();

  public static final DiseaseTestData DISEASE = new DiseaseTestData();
  public static final DiseaseLogTestData DISEASE_LOG = new DiseaseLogTestData();

  public static final PetTestData PET = new PetTestData();
  public static final PetTypeTestData PET_TYPE = new PetTypeTestData();

  public static final SymptomTestData SYMPTOM = new SymptomTestData();
  public static final SymptomLogTestData SYMPTOM_LOG = new SymptomLogTestData();

  public static final UserTestData USER = new UserTestData();

  public static User getUser() {
    return REGULAR_USER;
  }

  public static User getModerator() {
    return MODERATOR;
  }

  public static User getAdmin() {
    return ADMIN;
  }
}
