package com.laila.pet_symptom_tracker.mainconfig;

public class Routes {
  public static final String BASE_ROUTE = "/api/v1";
  // authentication
  public static final String LOGIN = BASE_ROUTE + "/login";
  public static final String REGISTER = BASE_ROUTE + "/register";
  // users
  public static final String USERS = BASE_ROUTE + "/users";
  public static final String USERS_SUB_PATH = addSingleSubPath(USERS);
  public static final String USERS_ALL_SUB_PATHS = addAllSubPaths(USERS);
  // pets
  public static final String PETS = BASE_ROUTE + "/pets";
  public static final String PETS_SUB_PATH = addSingleSubPath(PETS);
  public static final String PETS_ALL_SUB_PATHS = addAllSubPaths(PETS);
  // pet types
  public static final String PET_TYPES = BASE_ROUTE + "/pet-types";
  public static final String PET_TYPES_SUB_PATH = addSingleSubPath(PET_TYPES);
  public static final String PET_TYPES_ALL_SUB_PATHS = addAllSubPaths(PET_TYPES);
  // breeds
  public static final String BREEDS = BASE_ROUTE + "/breeds";
  public static final String BREEDS_SUB_PATH = addSingleSubPath(BREEDS);
  public static final String BREEDS_ALL_SUB_PATHS = addAllSubPaths(BREEDS);
  // diseases
  public static final String DISEASE = BASE_ROUTE + "/diseases";
  public static final String DISEASE_SUB_PATH = addSingleSubPath(DISEASE);
  public static final String DISEASE_ALL_SUB_PATHS = addAllSubPaths(DISEASE);
  // disease logs
  public static final String DISEASE_LOG = BASE_ROUTE + "/disease-logs";
  public static final String DISEASE_LOG_SUB_PATH = addSingleSubPath(DISEASE_LOG);
  public static final String DISEASE_LOG_ALL_SUB_PATHS = addAllSubPaths(DISEASE_LOG);
  // symptoms
  public static final String SYMPTOM = BASE_ROUTE + "/symptoms";
  public static final String SYMPTOM_SUB_PATH = addSingleSubPath(SYMPTOM);
  public static final String SYMPTOM_ALL_SUB_PATHS = addAllSubPaths(SYMPTOM);
  // symptom logs
  public static final String SYMPTOM_LOG = BASE_ROUTE + "/symptom-logs";
  public static final String SYMPTOM_LOG_SUB_PATH = addSingleSubPath(SYMPTOM_LOG);
  public static final String SYMPTOM_LOG_ALL_SUB_PATHS = addAllSubPaths(SYMPTOM_LOG);
  // black listed words
  public static final String BLACK_LISTED_WORDS = BASE_ROUTE + "/black-listed-words";
  public static final String BLACK_LISTED_WORDS_SUB_PATH = addSingleSubPath(BLACK_LISTED_WORDS);
  public static final String BLACK_LISTED_WORDS_ALL_SUB_PATHS = addAllSubPaths(BLACK_LISTED_WORDS);

  //  public static final String PLACEHOLDER = BASE_ROUTE + "/placeholder" ;

  private static String addSingleSubPath(String path) {
    return path + "/*";
  }

  private static String addAllSubPaths(String path) {
    return path + "/**";
  }
}
