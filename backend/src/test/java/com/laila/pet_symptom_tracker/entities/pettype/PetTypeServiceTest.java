package com.laila.pet_symptom_tracker.entities.pettype;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_OR_MODERATOR_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.testdata.TestData.INVALID_ID;
import static com.laila.pet_symptom_tracker.testdata.TestData.VALID_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PatchPetType;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PetTypeResponse;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PostPetType;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
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
class PetTypeServiceTest {
  @Mock PetTypeRepository petTypeRepository;
  @Mock AuthenticationService authenticationService;
  @InjectMocks PetTypeService petTypeService;

  @Test
  public void create_pet_type_as_user_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    PostPetType requestBody = TestDataProvider.PET_TYPE.getPostPetType();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> petTypeService.create(requestBody));
    assertEquals(ADMIN_OR_MODERATOR_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void create_pet_type_as_moderator_returns_pet_type_response() {
    User moderator = TestDataProvider.getModerator();
    PostPetType requestBody = TestDataProvider.PET_TYPE.getPostPetType();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);

    PetTypeResponse result = petTypeService.create(requestBody);
    assertNotNull(result);

    assertEquals(petType.getName(), result.name());
  }

  @Test
  public void create_pet_type_as_admin_returns_pet_type_response() {
    User admin = TestDataProvider.getAdmin();
    PostPetType requestBody = TestDataProvider.PET_TYPE.getPostPetType();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    PetTypeResponse result = petTypeService.create(requestBody);
    assertNotNull(result);

    assertEquals(petType.getName(), result.name());
  }

  @Test
  public void get_pet_type_by_id_with_invalid_id_should_throw_not_found_exception() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(petTypeRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> petTypeService.getById(INVALID_ID));
  }

  @Test
  public void get_pet_type_by_id_should_return_pet_type_response() {
    User admin = TestDataProvider.getAdmin();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(petTypeRepository.findById(VALID_ID)).thenReturn(Optional.of(petType));

    PetTypeResponse result = petTypeService.getById(VALID_ID);
    assertNotNull(result);
    assertEquals(petType.getName(), result.name());
  }

  @Test
  public void get_pet_type_by_id_as_user_should_not_return_soft_deleted_pet_type() {
    User user = TestDataProvider.getUser();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petTypeRepository.findByIdAndDeletedFalse(VALID_ID)).thenReturn(Optional.of(petType));

    petTypeService.getById(VALID_ID);

    verify(petTypeRepository).findByIdAndDeletedFalse(VALID_ID);
  }

  @Test
  public void get_pet_type_by_id_as_admin_should_return_soft_deleted_pet_type() {
    User admin = TestDataProvider.getAdmin();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(petTypeRepository.findById(VALID_ID)).thenReturn(Optional.of(petType));

    petTypeService.getById(VALID_ID);

    verify(petTypeRepository).findById(VALID_ID);
  }

  @Test
  public void get_all_pet_types_as_user_should_not_return_soft_deleted_pet_types() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    petTypeService.getAll();

    verify(petTypeRepository).findByDeletedFalse();
  }

  @Test
  public void get_all_pet_types_as_admin_should_return_all_pet_types() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    petTypeService.getAll();

    verify(petTypeRepository).findAll();
  }

  @Test
  public void patch_pet_type_with_invalid_id_should_throw_not_found_exception() {
    PatchPetType patch = TestDataProvider.PET_TYPE.getPatchPetType();

    when(petTypeRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> petTypeService.patch(INVALID_ID, patch));
  }

  @Test
  public void patch_pet_type_with_valid_id_should_return_patched_pet_type() {
    PatchPetType patch = TestDataProvider.PET_TYPE.getPatchPetType();
    PetType petType = TestDataProvider.PET_TYPE.getPetType();

    when(petTypeRepository.findById(VALID_ID)).thenReturn(Optional.of(petType));

    PetTypeResponse result = petTypeService.patch(VALID_ID, patch);

    assertEquals(patch.name(), result.name());
  }

  @Test
  public void delete_pet_type_with_invalid_id_should_throw_not_found_exception() {
    when(petTypeRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> petTypeService.delete(INVALID_ID));
  }

  @Test
  public void delete_pet_type_with_should_delete_pet_type() {
    PetType petType = TestDataProvider.PET_TYPE.getPetType();
    when(petTypeRepository.findById(VALID_ID)).thenReturn(Optional.of(petType));

    petTypeService.delete(VALID_ID);

    verify(petTypeRepository).deleteById(VALID_ID);
  }
}
