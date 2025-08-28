package com.laila.pet_symptom_tracker.entities.pet;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.*;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.breed.Breed;
import com.laila.pet_symptom_tracker.entities.breed.BreedRepository;
import com.laila.pet_symptom_tracker.entities.pet.dto.PatchPet;
import com.laila.pet_symptom_tracker.entities.pet.dto.PetResponse;
import com.laila.pet_symptom_tracker.entities.pet.dto.PostPet;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.entities.user.UserRepository;
import com.laila.pet_symptom_tracker.exceptions.generic.BadRequestException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NoContentException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.testdata.TestDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PetServiceTest {
  @InjectMocks PetService petService;

  @Mock PetRepository petRepository;
  @Mock BreedRepository breedRepository;
  @Mock UserRepository userRepository;
  @Mock AuthenticationService authenticationService;

  @Test
  public void create_pet_with_non_existing_breed_should_throw_bad_request_exception_with_message() {
    User user = TestDataProvider.getUser();
    PostPet requestBody = TestDataProvider.PET.getPostPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(breedRepository.findById(requestBody.breedId())).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> petService.create(requestBody));
    assertEquals(NON_EXISTENT_BREED, exception.getMessage());
  }

  @Test
  public void create_pet_returns_created_pet() {
    User user = TestDataProvider.getUser();
    PostPet requestBody = PET.getPostPet();
    Breed breed = BREED.getBreed();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(breedRepository.findById(requestBody.breedId())).thenReturn(Optional.of(breed));

    PetResponse result = petService.create(requestBody);

    assertNotNull(result);
    assertEquals(requestBody.name(), result.name());
  }

  @Test
  public void get_non_existing_pet_by_id_should_throw_not_found_exception() {
    when(petRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> petService.getById(INVALID_ID));
  }

  @Test
  public void get_pet_as_non_owner_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    Pet pet = PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> petService.getById(VALID_ID));

    assertEquals(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void get_pet_as_owner_returns_pet() {
    User user = TestDataProvider.getUser();
    Pet pet = TestDataProvider.PET.getPet(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    PetResponse result = petService.getById(VALID_ID);
    assertNotNull(result);

    assertEquals(pet.getOwner(), user);
    assertEquals(pet.getId(), result.id());
    assertEquals(pet.getName(), result.name());
  }

  // TODO get all tests
  @Test
  public void get_all_pets_should_return_throw_no_content_exception_if_no_pets_are_owned() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findByOwnerId(user.getId())).thenReturn(new ArrayList<Pet>());

    assertThrows(NoContentException.class, () -> petService.getAll());
  }

  @Test
  public void get_all_pets_as_moderator_should_return_all_pets() {
    User moderator = TestDataProvider.getModerator();
    List<Pet> petList = TestDataProvider.PET.getPetList();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    when(petRepository.findAll()).thenReturn(petList);

    List<PetResponse> result = petService.getAll();
    assertEquals(petList.size(), result.size());
  }

  @Test
  public void get_all_pets_as_admin_should_return_all_pets() {
    User admin = TestDataProvider.getAdmin();
    List<Pet> petList = TestDataProvider.PET.getPetList();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(petRepository.findAll()).thenReturn(petList);

    List<PetResponse> result = petService.getAll();
    assertEquals(petList.size(), result.size());
  }

  @Test
  public void get_all_pets_as_user_should_return_owned_pets_only() {
    User user = TestDataProvider.getUser();
    List<Pet> petList = TestDataProvider.PET.getOwnedPetList(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findByOwnerId(user.getId())).thenReturn(petList);

    List<PetResponse> result = petService.getAll();
    assertEquals(petList.size(), result.size());
  }

  @Test
  public void update_pet_with_invalid_id_should_throw_not_found_exception() {
    PatchPet patch = TestDataProvider.PET.getPatchPet();

    when(petRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> petService.update(INVALID_ID, patch));
  }

  @Test
  public void patch_non_owned_pet_as_user_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    PatchPet patch = TestDataProvider.PET.getPatchPet();
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> petService.update(VALID_ID, patch));

    assertEquals(OWNER_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void transfer_ownership_to_non_existent_user_should_throw_bad_request_exception() {
    User owner = TestDataProvider.getUser();
    PatchPet patch = new PatchPet(INVALID_USER_ID, "Garfield", CREATION_DATE, true, null, VALID_ID);
    Pet pet = TestDataProvider.PET.getPet(owner);

    when(authenticationService.getAuthenticatedUser()).thenReturn(owner);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> petService.update(VALID_ID, patch));
    assertEquals("Can not transfer ownership to non-existent user.", exception.getMessage());
  }

  @Test
  public void patch_with_invalid_breed_id_should_throw_bad_request_exception() {
    User owner = TestDataProvider.getUser();
    User newOwner = TestDataProvider.getModerator();
    PatchPet patch =
        new PatchPet(newOwner.getId(), "Garfield", CREATION_DATE, true, null, INVALID_ID);
    Pet pet = TestDataProvider.PET.getPet(owner);

    when(authenticationService.getAuthenticatedUser()).thenReturn(owner);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(userRepository.findById(newOwner.getId())).thenReturn(Optional.of(newOwner));
    when(breedRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> petService.update(VALID_ID, patch));
  }

  @Test
  public void patch_pet_should_return_patched_pet() {
    User owner = TestDataProvider.getUser();
    User newOwner = TestDataProvider.getModerator();
    PatchPet patch =
        new PatchPet(newOwner.getId(), "Snarfield", CREATION_DATE, true, null, VALID_ID);
    Pet pet = TestDataProvider.PET.getPet(owner);
    Breed breed = TestDataProvider.BREED.getBreed();

    when(authenticationService.getAuthenticatedUser()).thenReturn(owner);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(userRepository.findById(newOwner.getId())).thenReturn(Optional.of(newOwner));
    when(breedRepository.findById(VALID_ID)).thenReturn(Optional.of(breed));

    PetResponse result = petService.update(VALID_ID, patch);
    assertNotNull(result);

    assertEquals(patch.name(), result.name());
  }

  @Test
  public void delete_non_existent_pet_should_throw_not_found_exception() {
    when(petRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> petService.delete(INVALID_ID));
  }

  @Test
  public void delete_pet_as_non_owner_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> petService.delete(VALID_ID));

    assertEquals(OWNER_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void admin_should_be_able_to_delete_non_owned_pet() {
    User admin = TestDataProvider.getAdmin();
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    petService.delete(VALID_ID);

    verify(petRepository).deleteById(VALID_ID);
  }
}
