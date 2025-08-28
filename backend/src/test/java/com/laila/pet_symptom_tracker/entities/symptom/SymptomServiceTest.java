package com.laila.pet_symptom_tracker.entities.symptom;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_OR_MODERATOR_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.PATCH_DELETED_ENTITY;
import static com.laila.pet_symptom_tracker.testdata.TestData.INVALID_ID;
import static com.laila.pet_symptom_tracker.testdata.TestData.VALID_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PatchSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PostSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.SymptomResponse;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.exceptions.generic.PatchDeletedEntityException;
import com.laila.pet_symptom_tracker.testdata.TestDataProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class SymptomServiceTest {
  @InjectMocks SymptomService symptomService;
  @Mock SymptomRepository symptomRepository;
  @Mock AuthenticationService authenticationService;

  @Test
  public void create_symptom_should_save_and_return_symptom() {
    User admin = TestDataProvider.getAdmin();
    PostSymptom requestBody = TestDataProvider.SYMPTOM.getPostSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    symptomService.create(requestBody);

    ArgumentCaptor<Symptom> symptomCaptor = ArgumentCaptor.forClass(Symptom.class);
    verify(symptomRepository).save(symptomCaptor.capture());
  }

  @Test
  public void create_symptom_as_moderator_should_return_verified_symptom() {
    User user = TestDataProvider.getModerator();
    PostSymptom requestBody = TestDataProvider.SYMPTOM.getPostSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    SymptomResponse result = symptomService.create(requestBody);

    assertEquals(true, result.isVerified());
  }

  @Test
  public void create_symptom_as_admin_should_return_verified_symptom() {
    User user = TestDataProvider.getAdmin();
    PostSymptom requestBody = TestDataProvider.SYMPTOM.getPostSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    SymptomResponse result = symptomService.create(requestBody);

    assertEquals(true, result.isVerified());
  }

  @Test
  public void create_symptom_as_user_should_return_unverified_symptom() {
    User user = TestDataProvider.getUser();
    PostSymptom requestBody = TestDataProvider.SYMPTOM.getPostSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    SymptomResponse result = symptomService.create(requestBody);

    assertEquals(false, result.isVerified());
  }

  @Test
  public void get_symptom_with_invalid_id_should_throw_not_found_exception() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomRepository.findByIdAndDeletedFalse(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> symptomService.getById(INVALID_ID));
  }

  @Test
  public void get_symptom_by_id_should_return_symptom() {
    User user = TestDataProvider.getUser();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomRepository.findByIdAndDeletedFalse(VALID_ID)).thenReturn(Optional.of(symptom));

    SymptomResponse result = symptomService.getById(VALID_ID);
    assertNotNull(result);
  }

  @Test
  public void get_symptom_by_id_as_user_should_not_return_soft_deleted_symptom() {
    User user = TestDataProvider.getUser();
    Symptom deletedSymptom = TestDataProvider.SYMPTOM.getSoftDeletedSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomRepository.findByIdAndDeletedFalse(VALID_ID))
        .thenReturn(Optional.of(deletedSymptom));

    symptomService.getById(VALID_ID);

    verify(symptomRepository).findByIdAndDeletedFalse(VALID_ID);
  }

  @Test
  public void get_symptom_by_id_as_admin_should_return_soft_deleted_symptom() {
    User admin = TestDataProvider.getAdmin();
    Symptom deletedSymptom = TestDataProvider.SYMPTOM.getSoftDeletedSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(deletedSymptom));

    symptomService.getById(VALID_ID);

    verify(symptomRepository).findById(VALID_ID);
  }

  @Test
  public void get_all_symptoms_as_user_should_return_non_deleted_symptoms() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    symptomService.getAll();
    verify(symptomRepository).findByDeletedFalse();
  }

  @Test
  public void get_all_symptoms_as_admin_should_return_all_symptoms() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    symptomService.getAll();

    verify(symptomRepository).findAll();
  }

  @Test
  public void update_symptom_with_invalid_id_should_throw_not_found_exception() {
    PatchSymptom patch = TestDataProvider.SYMPTOM.getPatchSymptom();
    when(symptomRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> symptomService.update(INVALID_ID, patch));
  }

  @Test
  public void update_deleted_symptom_should_throw_patch_deleted_exception() {
    PatchSymptom patch = TestDataProvider.SYMPTOM.getPatchSymptom();
    Symptom deletedSymptom = TestDataProvider.SYMPTOM.getSoftDeletedSymptom();

    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(deletedSymptom));

    PatchDeletedEntityException exception =
        assertThrows(
            PatchDeletedEntityException.class, () -> symptomService.update(VALID_ID, patch));
    assertEquals(PATCH_DELETED_ENTITY, exception.getMessage());
  }

  @Test
  public void
      patch_symptom_verified_status_by_non_admin_should_throw_forbidden_exception_with_message() {
    PatchSymptom patch = TestDataProvider.SYMPTOM.getPatchSymptomWithVerifiedTrue();
    User user = TestDataProvider.getUser();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> symptomService.update(VALID_ID, patch));
    assertEquals(ADMIN_OR_MODERATOR_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void patch_symptom_verified_status_as_admin_should_work() {
    PatchSymptom patch = TestDataProvider.SYMPTOM.getPatchSymptomWithVerifiedTrue();
    User admin = TestDataProvider.getAdmin();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    SymptomResponse result = symptomService.update(VALID_ID, patch);
    assertNotNull(result);
    assertEquals(true, result.isVerified());
  }

  @Test
  public void patch_symptom_should_save_patched_symptom() {
    PatchSymptom patch = TestDataProvider.SYMPTOM.getPatchSymptom();
    User user = TestDataProvider.getUser();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    symptomService.update(VALID_ID, patch);

    ArgumentCaptor<Symptom> symptomCaptor = ArgumentCaptor.forClass(Symptom.class);
    verify(symptomRepository).save(symptomCaptor.capture());
  }

  @Test
  public void patch_symptom_should_return_correct_dto() {
    PatchSymptom patch = TestDataProvider.SYMPTOM.getPatchSymptom();
    User user = TestDataProvider.getUser();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    SymptomResponse result = symptomService.update(VALID_ID, patch);
    assertNotNull(result);

    assertEquals(patch.name(), result.name());
  }

  @Test
  public void delete_symptom_with_invalid_id_should_throw_not_found_exception() {
    when(symptomRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> symptomService.delete(INVALID_ID));
  }

  @Test
  public void delete_symptom_should_work() {
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    symptomService.delete(VALID_ID);

    verify(symptomRepository).deleteById(VALID_ID);
  }
}
