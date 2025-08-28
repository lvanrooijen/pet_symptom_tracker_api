package com.laila.pet_symptom_tracker.entities.breed;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.NON_EXISTENT_PET_TYPE;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.INVALID_ID;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.VALID_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.breed.dto.BreedResponse;
import com.laila.pet_symptom_tracker.entities.breed.dto.PatchBreed;
import com.laila.pet_symptom_tracker.entities.breed.dto.PostBreed;
import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import com.laila.pet_symptom_tracker.entities.pettype.PetTypeRepository;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.BadRequestException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.testdata.TestDataProvider;
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
class BreedServiceTest {
  @Mock private BreedRepository breedRepository;
  @Mock private PetTypeRepository petTypeRepository;
  @Mock private AuthenticationService authenticationService;

  @InjectMocks BreedService breedService;

  @Test
  public void create_breed_should_not_return_null() {
    PostBreed postBreed = TestDataProvider.BREED.getPostBreed();
    User admin = TestDataProvider.getAdmin();
    PetType petType = TestDataProvider.PET_TYPE.getPetType(admin);

    when(petTypeRepository.findById(postBreed.petTypeId())).thenReturn(Optional.of(petType));
    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(breedRepository.save(any(Breed.class))).thenReturn(new Breed());

    BreedResponse result = breedService.create(postBreed);

    assertNotNull(result);
    verify(breedRepository).save(any(Breed.class));
  }

  @Test
  public void
      create_breed_with_non_existing_pet_type_throws_exception_and_has_correct_exception_message() {
    PostBreed postBreed = TestDataProvider.BREED.getPostBreed();

    when(petTypeRepository.findById(postBreed.petTypeId())).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> breedService.create(postBreed));

    assertEquals(NON_EXISTENT_PET_TYPE, exception.getMessage());
  }

  @Test
  public void get_non_existent_breed_by_id_throws_not_found_exception() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(breedRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> breedService.getById(INVALID_ID));
  }

  @Test
  public void get_breed_by_id_as_admin_should_show_soft_deleted_breed() {
    User admin = TestDataProvider.getAdmin();
    Breed breed = TestDataProvider.BREED.getBreed();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(breedRepository.findById(VALID_ID)).thenReturn(Optional.of(breed));

    breedService.getById(VALID_ID);

    verify(breedRepository).findById(VALID_ID);
  }

  @Test
  public void get_breed_by_id_as_user_should_show_not_soft_deleted_breed() {
    User user = TestDataProvider.getUser();
    Breed breed = TestDataProvider.BREED.getBreed();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(breedRepository.findByIdAndDeletedFalse(VALID_ID)).thenReturn(Optional.of(breed));

    breedService.getById(VALID_ID);

    verify(breedRepository).findByIdAndDeletedFalse(VALID_ID);
  }

  @Test
  public void updateBreed_breed_with_invalid_id_throws_not_found_exception() {
    PatchBreed patch = TestDataProvider.BREED.getPatchBreed();

    when(breedRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> breedService.updateBreed(INVALID_ID, patch));
  }

  @Test
  public void
      updateBreed_breed_with_invalid_pet_type_id_should_throw_bad_request_exception_with_correct_message() {
    Breed breed = TestDataProvider.BREED.getBreed();
    PatchBreed patch = TestDataProvider.BREED.getPatchBreed(INVALID_ID);

    when(breedRepository.findById(VALID_ID)).thenReturn(Optional.of(breed));
    when(petTypeRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> breedService.updateBreed(VALID_ID, patch));
    assertEquals(NON_EXISTENT_PET_TYPE, exception.getMessage());
  }

  @Test
  public void updateBreed_breed_should_return_patched_breed() {
    Breed breed = TestDataProvider.BREED.getBreed();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();
    PatchBreed patch = TestDataProvider.BREED.getPatchBreed();

    when(breedRepository.findById(VALID_ID)).thenReturn(Optional.of(breed));
    when(petTypeRepository.findById(VALID_ID)).thenReturn(Optional.of(petType));

    BreedResponse result = breedService.updateBreed(VALID_ID, patch);

    assertEquals(patch.name(), result.name());
    assertEquals(patch.petTypeId(), result.petType().id());
  }

  @Test
  public void delete_by_id_with_invalid_id_throws_not_found_exception() {
    when(breedRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> breedService.deleteById(INVALID_ID));
  }

  @Test
  public void get_all_as_admin_should_call_return_soft_deleted_breeds() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    breedService.getAll();

    verify(breedRepository).findAll();
  }

  @Test
  public void get_all_as_user_should_return_non_deleted_breeds_only() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    breedService.getAll();

    verify(breedRepository).findByDeletedFalse();
  }
}
