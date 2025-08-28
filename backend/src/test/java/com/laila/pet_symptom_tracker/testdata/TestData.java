package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.authentication.Role;
import com.laila.pet_symptom_tracker.entities.breed.Breed;
import com.laila.pet_symptom_tracker.entities.disease.Disease;
import com.laila.pet_symptom_tracker.entities.disease.dto.DiseaseCompactResponse;
import com.laila.pet_symptom_tracker.entities.diseaselog.DiseaseLog;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.pet.dto.PetCompactResponse;
import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import com.laila.pet_symptom_tracker.entities.symptom.Symptom;
import com.laila.pet_symptom_tracker.entities.symptomlog.SymptomLog;
import com.laila.pet_symptom_tracker.entities.user.User;
import java.time.LocalDate;
import java.util.UUID;

public abstract class TestData {
  // id's
  public static final UUID REGULAR_USER_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000001");
  public static final UUID MODERATOR_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
  public static final UUID ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
  public static final UUID DEFAULT_USER_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000004");
  public static final UUID INVALID_USER_ID =
      UUID.fromString("99999999-9999-9999-9999-999999999999");
  public static final Long VALID_ID = 1L;
  public static final Long INVALID_ID = 999L;

  public static final LocalDate CREATION_DATE = LocalDate.of(2021, 1, 1);
  public static final LocalDate HEAL_DATE = LocalDate.of(2025, 1, 1);

  protected static final User DEFAULT_USER =
      User.builder()
          .id(DEFAULT_USER_ID)
          .email("default@email.com")
          .password("Password123!")
          .username("default")
          .firstName("firstname")
          .lastName("lastname")
          .role(Role.USER)
          .build();

  protected static User REGULAR_USER =
      User.builder()
          .id(REGULAR_USER_ID)
          .email("default@email.com")
          .password("Password123!")
          .username("user")
          .role(Role.USER)
          .build();

  protected static User MODERATOR =
      User.builder()
          .id(MODERATOR_ID)
          .email("default@email.com")
          .password("Password123!")
          .username("moderator")
          .role(Role.MODERATOR)
          .build();

  protected static User ADMIN =
      User.builder()
          .id(ADMIN_ID)
          .email("default@email.com")
          .password("Password123!")
          .username("admin")
          .role(Role.ADMIN)
          .build();

  protected static PetType DEFAULT_PET_TYPE = new PetType(VALID_ID, "Siamese", false, DEFAULT_USER);

  protected static Breed DEFAULT_BREED =
      Breed.builder().name("Sphinx").petType(DEFAULT_PET_TYPE).creator(DEFAULT_USER).build();

  protected static Pet DEFAULT_PET =
      Pet.builder()
          .name("Garfield")
          .breed(DEFAULT_BREED)
          .id(VALID_ID)
          .owner(DEFAULT_USER)
          .isAlive(true)
          .dateOfBirth(CREATION_DATE)
          .build();

  protected static PetCompactResponse DEFAULT_PET_COMPACT =
      new PetCompactResponse(VALID_ID, DEFAULT_PET.getName());

  protected static Disease DEFAULT_DISEASE =
      new Disease(VALID_ID, "Rabies", "description on rabies", false, DEFAULT_USER);

  protected static DiseaseCompactResponse DEFAULT_DISEASE_COMPACT =
      new DiseaseCompactResponse(
          VALID_ID, DEFAULT_DISEASE.getName(), DEFAULT_DISEASE.getDescription());

  protected static DiseaseLog DEFAULT_DISEASE_LOG =
      DiseaseLog.builder().pet(DEFAULT_PET).disease(DEFAULT_DISEASE).build();

  protected static Symptom DEFAULT_SYMPTOM =
      new Symptom(VALID_ID, "Diarrhea", "Fluid poops", false, false);

  protected static SymptomLog DEFAULT_SYMPTOM_LOG =
      new SymptomLog(VALID_ID, DEFAULT_PET, DEFAULT_SYMPTOM, "Had a greenish color", CREATION_DATE);
}
