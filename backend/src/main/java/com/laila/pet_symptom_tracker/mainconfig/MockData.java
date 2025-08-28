package com.laila.pet_symptom_tracker.mainconfig;

import com.laila.pet_symptom_tracker.entities.authentication.Role;
import com.laila.pet_symptom_tracker.entities.breed.dto.PostBreed;
import com.laila.pet_symptom_tracker.entities.disease.dto.PostDisease;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PostPetType;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PostSymptom;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PostSymptomLog;
import com.laila.pet_symptom_tracker.entities.user.User;
import java.time.LocalDate;
import java.util.List;

public class MockData {

  public static List<User> getUsers() {
    return List.of(
        new User("charlie@gmail.com", "Password123!", "Charlie", Role.USER),
        new User("carol@gmail.com", "Password123!", "Carol", Role.USER),
        new User("dave@gmail.com", "Password123!", "Dave", Role.USER),
        new User("bob@gmail.com", "Password123!", "Bob", Role.USER),
        new User("luna@gmail.com", "Password123!", "Luna", Role.USER),
        new User("milo@gmail.com", "Password123!", "Milo", Role.USER),
        new User("sophie@gmail.com", "Password123!", "Sophie", Role.USER),
        new User("max@gmail.com", "Password123!", "Max", Role.USER),
        new User("olivia@gmail.com", "Password123!", "Olivia", Role.USER),
        new User("admin@gmail.com", "Password123!", "Admin", Role.ADMIN),
        new User("moderator@gmail.com", "Password123!", "Moderator", Role.MODERATOR),
        new User("modOne@gmail.com", "Password123!", "ModOne", Role.MODERATOR),
        new User("modTwo@gmail.com", "Password123!", "ModTwo", Role.MODERATOR),
        new User("user@gmail.com", "Password123!", "user", Role.USER));
  }

  public static List<Pet> getPets() {
    return List.of(
        new Pet("Mister Snugglebut", LocalDate.of(2019, 6, 2), true),
        new Pet("Fluffles", LocalDate.of(2004, 9, 28), true),
        new Pet("Kitiko", LocalDate.of(2012, 2, 11), true),
        new Pet("MoonCalf", LocalDate.of(2012, 2, 11), true),
        new Pet("Mimi", LocalDate.of(2012, 2, 11), true),
        new Pet("Momo", LocalDate.of(2012, 2, 11), true),
        new Pet("MissStealsYourPizza", LocalDate.of(2012, 2, 11), true),
        new Pet("Josephine", LocalDate.of(2012, 2, 11), true),
        new Pet("Tupac", LocalDate.of(2012, 2, 11), true),
        new Pet("TunaAddict", LocalDate.of(2012, 2, 11), true),
        new Pet("CrazyCatNip", LocalDate.of(2012, 2, 11), true),
        new Pet("WavyLady", LocalDate.of(2012, 2, 11), true),
        new Pet("Lama", LocalDate.of(2012, 2, 11), true),
        new Pet("Tuxie", LocalDate.of(2008, 11, 13), true));
  }

  public static List<PostPetType> getPetTypes() {
    return List.of(
        new PostPetType("Cat"),
        new PostPetType("Dog"),
        new PostPetType("Rabbit"),
        new PostPetType("Bird"));
  }

  public static List<PostBreed> getBreeds() {
    return List.of(
        new PostBreed("Siamese", 1L),
        new PostBreed("Rotweiler", 2L),
        new PostBreed("Flemish Giant", 3L),
        new PostBreed("Parrot", 4L));
  }

  public static List<PostDisease> getDiseases() {
    return List.of(
        new PostDisease("Rabies", "A deadly virus that affects the nervous system of mammals."),
        new PostDisease(
            "Canine Parvovirus", "A severe virus in dogs causing vomiting and diarrhea."),
        new PostDisease(
            "Feline Leukemia Virus (FeLV)", "A virus in cats that weakens the immune system."),
        new PostDisease(
            "Foot-and-Mouth Disease (FMD)",
            "A highly contagious virus affecting livestock like cows, pigs, and sheep."),
        new PostDisease(
            "Avian Influenza (Bird Flu)",
            "A flu virus that affects birds and sometimes spreads to humans."));
  }

  public static List<PostSymptom> getSymptoms() {
    return List.of(
        new PostSymptom(
            "Diarrhea",
            "Diarrhea, also spelled diarrhoea or diarrhœa, is the condition of having at least three loose, liquid, or watery bowel movements in a day. It often lasts for a few days and can result in dehydration due to fluid loss."),
        new PostSymptom(
            "Vomiting",
            "Vomiting is the involuntary, forceful expulsion of the contents of one's stomach through the mouth and sometimes the nose."),
        new PostSymptom(
            "Cough",
            "A cough is a sudden expulsion of air through the large breathing passages which can help clear them of fluids, irritants, foreign particles and microbes."));
  }

  public static List<PostSymptomLog> getSymptomLogs() {
    return List.of(
        new PostSymptomLog(null, null, "chat chatter chat", LocalDate.now()),
        new PostSymptomLog(null, null, "bladibla", LocalDate.now()),
        new PostSymptomLog(null, null, "soweto", LocalDate.now()));
  }
}
