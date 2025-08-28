package com.laila.pet_symptom_tracker.mainconfig;

import static com.laila.pet_symptom_tracker.mainconfig.Routes.*;

public class SecurityPaths {

  public static final String[] OPEN_POST_PATHS = {LOGIN, REGISTER};
  public static final String[] AUTHENTICATED_GET_PATHS = {
    PETS, PETS_SUB_PATH, BREEDS, BREEDS_SUB_PATH, PET_TYPES
  };
  public static final String[] AUTHENTICATED_MANAGE_PATHS = {
    PETS, USERS,
  };
  public static final String[] MODERATOR_PATHS = {};

  public static final String[] MODERATOR_MANAGE_PATHS = {
    PET_TYPES, PET_TYPES_SUB_PATH, BREEDS, BREEDS_SUB_PATH, DISEASE, DISEASE_SUB_PATH
  };

  public static String[] ADMIN_ONLY_PATHS = {
    USERS, BLACK_LISTED_WORDS, "/actuator/*/**", "/swagger-ui/**", "/v3/api-docs*/**"
  };
}
