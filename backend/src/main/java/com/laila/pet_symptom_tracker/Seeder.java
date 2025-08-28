package com.laila.pet_symptom_tracker;

import com.laila.pet_symptom_tracker.entities.authentication.Role;
import com.laila.pet_symptom_tracker.entities.blacklistword.BlackListWord;
import com.laila.pet_symptom_tracker.entities.blacklistword.BlackListWordRepository;
import com.laila.pet_symptom_tracker.entities.breed.Breed;
import com.laila.pet_symptom_tracker.entities.breed.BreedRepository;
import com.laila.pet_symptom_tracker.entities.disease.Disease;
import com.laila.pet_symptom_tracker.entities.disease.DiseaseRepository;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.pet.PetRepository;
import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import com.laila.pet_symptom_tracker.entities.pettype.PetTypeRepository;
import com.laila.pet_symptom_tracker.entities.symptom.Symptom;
import com.laila.pet_symptom_tracker.entities.symptom.SymptomRepository;
import com.laila.pet_symptom_tracker.entities.symptomlog.SymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.SymptomLogRepository;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.entities.user.UserRepository;
import com.laila.pet_symptom_tracker.entities.user.UserService;
import com.laila.pet_symptom_tracker.mainconfig.MockData;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final Random random = new Random();
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final UserRepository userRepository;
  private final PetRepository petRepository;
  private final PetTypeRepository petTypeRepository;
  private final BreedRepository breedRepository;
  private final DiseaseRepository diseaseRepository;
  private final SymptomRepository symptomRepository;
  private final SymptomLogRepository symptomLogRepository;
  private final BlackListWordRepository blackListWordRepository;

  @Override
  public void run(String... args) throws Exception {
    seedUsers();
    seedDiseases();
    seedPetTypes();
    seedBreeds();
    seedPets();
    seedSymptoms();
    seedSymptomLogs();
    seedBlackListWords();
  }

  private void seedBlackListWords() {
    if (!blackListWordRepository.findAll().isEmpty()) return;

    List<BlackListWord> foulWords =
        List.of(new BlackListWord("fuck"), new BlackListWord("dick"), new BlackListWord("pussy"));
    blackListWordRepository.saveAll(foulWords);
  }

  private void seedSymptomLogs() {
    if (!symptomLogRepository.findAll().isEmpty()) return;
    List<Pet> pets = petRepository.findAll();
    List<SymptomLog> logs =
        MockData.getSymptomLogs().stream()
            .map(
                log ->
                    SymptomLog.builder()
                        .pet(pets.get(random.nextInt(0, pets.size())))
                        .details(log.details())
                        .reportDate(log.reportDate())
                        .build())
            .toList();
    symptomLogRepository.saveAll(logs);
  }

  private void seedSymptoms() {
    if (!symptomRepository.findAll().isEmpty()) return;
    List<Symptom> symptoms =
        MockData.getSymptoms().stream()
            .map(
                symptom ->
                    Symptom.builder()
                        .name(symptom.name())
                        .description(symptom.description())
                        .verified(false)
                        .build())
            .toList();
    symptomRepository.saveAll(symptoms);
  }

  private void seedDiseases() {
    if (!diseaseRepository.findAll().isEmpty()) return;
    List<User> moderators = userRepository.findByRole(Role.MODERATOR);
    if (moderators.isEmpty()) return;
    int maxMods = moderators.size() - 1;
    List<Disease> diseases =
        MockData.getDiseases().stream()
            .map(
                disease ->
                    Disease.builder()
                        .name(disease.name())
                        .description(disease.description())
                        .creator(moderators.get(random.nextInt(0, maxMods)))
                        .build())
            .toList();
    diseaseRepository.saveAll(diseases);
  }

  private void seedBreeds() {
    if (!breedRepository.findAll().isEmpty()) return;
    List<PetType> types = petTypeRepository.findAll();
    if (types.isEmpty()) return;
    List<User> moderators = userRepository.findByRole(Role.MODERATOR);
    if (moderators.isEmpty()) return;
    int maxMods = moderators.size() - 1;

    MockData.getBreeds()
        .forEach(
            breed -> {
              Breed created =
                  Breed.builder()
                      .name(breed.name())
                      .petType(petTypeRepository.findById(breed.petTypeId()).orElse(null))
                      .creator(moderators.get(random.nextInt(0, maxMods)))
                      .build();
              breedRepository.save(created);
            });
  }

  private void seedPetTypes() {
    if (!petTypeRepository.findAll().isEmpty()) return;
    List<User> moderators = userRepository.findByRole(Role.MODERATOR);
    if (moderators.isEmpty()) return;
    int maxMods = moderators.size() - 1;
    List<PetType> petTypes =
        MockData.getPetTypes().stream()
            .map(
                type ->
                    PetType.builder()
                        .name(type.name())
                        .creator(moderators.get(random.nextInt(0, maxMods)))
                        .build())
            .toList();

    petTypeRepository.saveAll(petTypes);
  }

  private void seedUsers() {
    List<User> users = MockData.getUsers();
    users.forEach(user -> user.setPassword(passwordEncoder.encode("Password123!")));
    users.forEach(userService::createUser);
  }

  private void seedPets() {
    if (!petRepository.findAll().isEmpty()) return;
    List<User> users = userRepository.findByRole(Role.USER);
    if (users.isEmpty()) return;
    List<Breed> breeds = breedRepository.findAll();

    List<Pet> pets = MockData.getPets();

    int maxBreed = breeds.size() - 1;
    int maxUsers = users.size() - 1;

    for (Pet pet : pets) {
      pet.setOwner(users.get(random.nextInt(0, maxUsers)));
      pet.setBreed(breeds.get(random.nextInt(0, maxBreed)));
    }

    petRepository.saveAll(pets);
  }
}
