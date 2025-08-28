package com.laila.pet_symptom_tracker.entities.symptomlog;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.*;
import static com.laila.pet_symptom_tracker.testdata.TestData.INVALID_ID;
import static com.laila.pet_symptom_tracker.testdata.TestData.VALID_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.pet.PetRepository;
import com.laila.pet_symptom_tracker.entities.symptom.Symptom;
import com.laila.pet_symptom_tracker.entities.symptom.SymptomRepository;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PatchSymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PostSymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.SymptomLogResponse;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.BadRequestException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.testdata.TestDataProvider;
import java.util.List;
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
class SymptomLogServiceTest {
  @InjectMocks SymptomLogService symptomLogService;
  @Mock SymptomLogRepository symptomLogRepository;
  @Mock SymptomRepository symptomRepository;
  @Mock PetRepository petRepository;
  @Mock AuthenticationService authenticationService;

  @Test
  public void create_pet_with_invalid_pet_id_should_throw_bad_request_with_message() {
    PostSymptomLog requestBody = TestDataProvider.SYMPTOM_LOG.getPostSymptomLogWithInvalidPetId();

    when(petRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> symptomLogService.create(requestBody));
    assertEquals(NON_EXISTENT_PET, exception.getMessage());
  }

  @Test
  public void create_symptom_log_as_non_owner_should_throw_forbidden_exception() {
    User user = TestDataProvider.getUser();
    PostSymptomLog requestBody = TestDataProvider.SYMPTOM_LOG.getPostSymptomLog();
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> symptomLogService.create(requestBody));
    assertEquals(OWNER_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void
      create_symptom_log_with_invalid_symptom_id_should_throw_bad_request_exception_with_message() {
    User user = TestDataProvider.getUser();
    PostSymptomLog requestBody =
        TestDataProvider.SYMPTOM_LOG.getPostSymptomLogWithInvalidSymptomId();
    Pet pet = TestDataProvider.PET.getPet(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(symptomRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> symptomLogService.create(requestBody));

    assertEquals(NON_EXISTENT_SYMPTOM, exception.getMessage());
  }

  @Test
  public void create_symptom_log_should_save() {
    User user = TestDataProvider.getUser();
    PostSymptomLog requestBody = TestDataProvider.SYMPTOM_LOG.getPostSymptomLog();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();
    Pet pet = TestDataProvider.PET.getPet(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    symptomLogService.create(requestBody);

    ArgumentCaptor<SymptomLog> symptomLogCaptor = ArgumentCaptor.forClass(SymptomLog.class);
    verify(symptomLogRepository).save(symptomLogCaptor.capture());
  }

  @Test
  public void create_symptom_log_should_saved_symptom_log() {
    User user = TestDataProvider.getUser();
    PostSymptomLog requestBody = TestDataProvider.SYMPTOM_LOG.getPostSymptomLog();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();
    Pet pet = TestDataProvider.PET.getPet(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    SymptomLogResponse result = symptomLogService.create(requestBody);
    assertNotNull(result);
    assertEquals(requestBody.details(), result.details());
  }

  @Test
  public void get_symptom_log_by_id_with_invalid_id_should_throw_not_found_exception() {
    when(symptomLogRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> symptomLogService.getById(INVALID_ID));
  }

  @Test
  public void get_symptom_log_by_id_as_non_owner_should_throw_forbidden_exception() {
    User user = TestDataProvider.getUser();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> symptomLogService.getById(VALID_ID));

    assertEquals(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void get_symptom_log_by_id_as_moderator_should_return_symptom_log() {
    User moderator = TestDataProvider.getModerator();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    SymptomLogResponse result = symptomLogService.getById(VALID_ID);

    assertNotNull(result);
    assertEquals(symptomLog.getDetails(), result.details());
  }

  @Test
  public void get_symptom_log_by_id_as_admin_should_return_symptom_log() {
    User admin = TestDataProvider.getAdmin();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    SymptomLogResponse result = symptomLogService.getById(VALID_ID);

    assertNotNull(result);
    assertEquals(symptomLog.getDetails(), result.details());
  }

  @Test
  public void find_all_symptom_logs_as_owner_should_return_owned_symptom_logs_only() {
    User user = TestDataProvider.getUser();
    SymptomLog ownedSymptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog(user);
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    List<SymptomLog> symptomLogList =
        List.of(ownedSymptomLog, ownedSymptomLog, symptomLog, symptomLog);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findAll()).thenReturn(symptomLogList);

    List<SymptomLogResponse> result = symptomLogService.findAll();
    assertEquals(2, result.size());
  }

  @Test
  public void update_symptom_log_with_invalid_id_should_throw_not_found_exception() {
    PatchSymptomLog patch = TestDataProvider.SYMPTOM_LOG.getPatchSymptomLog();

    when(symptomLogRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> symptomLogService.update(INVALID_ID, patch));
  }

  @Test
  public void
      update_symptom_log_with_invalid_pet_id_should_throw_bad_request_exception_with_message() {
    User user = TestDataProvider.getUser();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    PatchSymptomLog patch = new PatchSymptomLog(symptomLog.getDetails(), VALID_ID, INVALID_ID);
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));
    when(petRepository.findById(INVALID_ID)).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> symptomLogService.update(VALID_ID, patch));
    assertEquals(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void
      update_symptom_log_with_invalid_symptom_should_throw_bad_request_exception_with_message() {
    User user = TestDataProvider.getUser();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();
    Pet pet = TestDataProvider.PET.getPet(user);
    PatchSymptomLog patch = new PatchSymptomLog(symptomLog.getDetails(), INVALID_ID, VALID_ID);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));
    when(symptomRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> symptomLogService.update(VALID_ID, patch));
    assertEquals(NON_EXISTENT_SYMPTOM, exception.getMessage());
  }

  @Test
  public void update_non_owned_symptom_log_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    PatchSymptomLog patch = TestDataProvider.SYMPTOM_LOG.getPatchSymptomLog();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> symptomLogService.update(VALID_ID, patch));
    assertEquals(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void update_symptom_log_as_admin_should_save() {
    User admin = TestDataProvider.getAdmin();
    PatchSymptomLog patch = TestDataProvider.SYMPTOM_LOG.getPatchSymptomLog();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    Pet pet = TestDataProvider.PET.getPet();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    symptomLogService.update(VALID_ID, patch);

    ArgumentCaptor<SymptomLog> symptomLogCaptor = ArgumentCaptor.forClass(SymptomLog.class);
    verify(symptomLogRepository).save(symptomLogCaptor.capture());
  }

  @Test
  public void update_symptom_log_as_moderator_should_save() {
    User moderator = TestDataProvider.getModerator();
    PatchSymptomLog patch = TestDataProvider.SYMPTOM_LOG.getPatchSymptomLog();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    Pet pet = TestDataProvider.PET.getPet();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    symptomLogService.update(VALID_ID, patch);

    ArgumentCaptor<SymptomLog> symptomLogCaptor = ArgumentCaptor.forClass(SymptomLog.class);
    verify(symptomLogRepository).save(symptomLogCaptor.capture());
  }

  @Test
  public void updated_symptom_log_should_return_saved_symptom_log() {
    User moderator = TestDataProvider.getModerator();
    PatchSymptomLog patch = TestDataProvider.SYMPTOM_LOG.getPatchSymptomLog();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();
    Pet pet = TestDataProvider.PET.getPet();
    Symptom symptom = TestDataProvider.SYMPTOM.getSymptom();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));
    when(petRepository.findById(VALID_ID)).thenReturn(Optional.of(pet));
    when(symptomRepository.findById(VALID_ID)).thenReturn(Optional.of(symptom));

    SymptomLogResponse result = symptomLogService.update(VALID_ID, patch);
    assertNotNull(result);
  }

  @Test
  public void delete_symptom_log_with_invalid_id_should_throw_not_found_exception() {
    when(symptomLogRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> symptomLogService.delete(INVALID_ID));
  }

  @Test
  public void delete_symptom_log_as_non_owner_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> symptomLogService.delete(VALID_ID));
    assertEquals(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void delete_symptom_log_as_owner_should_call_delete() {
    User user = TestDataProvider.getUser();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    symptomLogService.delete(VALID_ID);

    verify(symptomLogRepository).deleteById(VALID_ID);
  }

  @Test
  public void delete_symptom_log_as_moderator_should_call_delete() {
    User moderator = TestDataProvider.getModerator();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    symptomLogService.delete(VALID_ID);

    verify(symptomLogRepository).deleteById(VALID_ID);
  }

  @Test
  public void delete_symptom_log_as_admin_should_call_delete() {
    User admin = TestDataProvider.getAdmin();
    SymptomLog symptomLog = TestDataProvider.SYMPTOM_LOG.getSymptomLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(symptomLogRepository.findById(VALID_ID)).thenReturn(Optional.of(symptomLog));

    symptomLogService.delete(VALID_ID);

    verify(symptomLogRepository).deleteById(VALID_ID);
  }
}
