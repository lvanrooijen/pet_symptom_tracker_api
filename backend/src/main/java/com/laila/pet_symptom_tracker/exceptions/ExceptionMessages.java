package com.laila.pet_symptom_tracker.exceptions;

public class ExceptionMessages {
  // authentication
  public static String ADMIN_ONLY_ACTION = "Only an admin is allowed to perform this action.";
  public static String ADMIN_OR_MODERATOR_ONLY_ACTION =
      "Only an admin or moderator is allowed to perform this action.";

  public static String OWNER_OR_ADMIN_ONLY_ACTION =
      "Only the owner or admin is allowed to perform this action";

  public static String OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION =
      "Only the owner, moderator or an admin  perform this action.";

  public static String OWNER_ONLY_ACTION = "Only the owner is allowed to perform this action.";

  public static String USER_NOT_REGISTERED = "A user with this email or username can not be found.";

  // bad requests and miscellaneous
  public static String NON_EXISTENT_DISEASE = "This disease does not exist.";
  public static String NON_EXISTENT_PET_TYPE = "This pet type does not exist.";
  public static String NON_EXISTENT_BREED = "This breed does not exist.";
  public static String NON_EXISTENT_PET = "This pet does not exist";
  public static String NON_EXISTENT_SYMPTOM = "This Symptom does not exist";

  public static String DUPLICATE_BLACKLIST_WORD = "This word is already on the blacklist.";
  public static String PATCH_DELETED_ENTITY = "Can not alter a deleted entity.";
}
