package com.laila.pet_symptom_tracker.entities.diseaselog;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.*;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.INVALID_ID;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.VALID_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.disease.Disease;
import com.laila.pet_symptom_tracker.entities.disease.DiseaseRepository;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.DiseaseLogResponse;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.PatchDiseaseLog;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.PostDiseaseLog;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.pet.PetRepository;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class DiseaseLogServiceTest {
  @Mock DiseaseLogRepository diseaseLogRepository;
  @Mock DiseaseRepository diseaseRepository;
  @Mock PetRepository petRepository;
  @Mock AuthenticationService authenticationService;

  @InjectMocks DiseaseLogService diseaseLogService;

  @Test
  public void
      create_disease_log_with_invalid_pet_id_should_throw_bad_request_exception_with_message() {
    PostDiseaseLog requestBody = TestDataProvider.DISEASE_LOG.getPostDiseaseLog();
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(requestBody.petId())).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> diseaseLogService.create(requestBody));
    assertEquals(NON_EXISTENT_PET_TYPE, exception.getMessage());
  }

  @Test
  public void create_when_not_owner_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    PostDiseaseLog requestBody = TestDataProvider.DISEASE_LOG.getPostDiseaseLog();
    Pet pet = TestDataProvider.PET.getPet();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(requestBody.petId())).thenReturn(Optional.of(pet));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> diseaseLogService.create(requestBody));

    assertEquals(OWNER_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void
      create_disease_log_with_invalid_disease_should_throw_bad_request_exception_with_message() {
    User user = TestDataProvider.getUser();
    PostDiseaseLog requestBody = TestDataProvider.DISEASE_LOG.getPostDiseaseLog();
    Pet pet = TestDataProvider.PET.getPet(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(requestBody.petId())).thenReturn(Optional.of(pet));
    when(diseaseRepository.findById(VALID_ID)).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> diseaseLogService.create(requestBody));
    assertEquals(NON_EXISTENT_DISEASE, exception.getMessage());
  }

  @Test
  public void create_disease_log_should_save_and_return_disease_log() {
    User user = TestDataProvider.getUser();
    PostDiseaseLog requestBody = TestDataProvider.DISEASE_LOG.getPostDiseaseLog();
    Pet pet = TestDataProvider.PET.getPet(user);
    Disease disease = TestDataProvider.DISEASE.getDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(petRepository.findById(requestBody.petId())).thenReturn(Optional.of(pet));
    when(diseaseRepository.findById(VALID_ID)).thenReturn(Optional.of(disease));

    DiseaseLogResponse result = diseaseLogService.create(requestBody);

    assertEquals(requestBody.petId(), result.pet().id());
    assertEquals(requestBody.diseaseId(), result.disease().id());
    assertEquals(requestBody.discoveryDate(), result.discoveryDate());
  }

  @Test
  public void get_all_disease_logs_as_owner_should_return_owned_logs() {
    User user = TestDataProvider.getUser();
    List<DiseaseLog> diseaseLogList = TestDataProvider.DISEASE_LOG.diseaseLogListWithOneOwner(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findAll()).thenReturn(diseaseLogList);

    List<DiseaseLogResponse> result = diseaseLogService.getAll();

    assertEquals(1, result.size());
  }

  @Test
  public void get_all_disease_logs_as_admin_should_return_all_logs() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    diseaseLogService.getAll();

    verify(diseaseLogRepository).findAll();
  }

  @Test
  public void get_disease_log_by_invalid_id_should_throw_not_found_exception() {
    User user = TestDataProvider.getUser();

    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> diseaseLogService.getById(VALID_ID));
  }

  @Test
  public void get_disease_log_by_id_by_non_owner_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> diseaseLogService.getById(VALID_ID));

    assertEquals(OWNER_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void get_disease_log_as_owner_by_id_should_return_disease_log() {
    User user = TestDataProvider.getUser();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog(user);

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));

    DiseaseLogResponse result = diseaseLogService.getById(VALID_ID);

    assertEquals(diseaseLog.getDisease().getId(), result.disease().id());
    assertEquals(diseaseLog.getPet().getId(), result.pet().id());
  }

  @Test
  public void get_disease_log_by_id_as_moderator_should_return_disease_log() {
    User moderator = TestDataProvider.getModerator();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog(moderator);

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));

    DiseaseLogResponse result = diseaseLogService.getById(VALID_ID);
    assertNotNull(result);
  }

  @Test
  public void get_disease_log_by_id_as_admin_should_return_disease_log() {
    User admin = TestDataProvider.getAdmin();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog(admin);

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));

    DiseaseLogResponse result = diseaseLogService.getById(VALID_ID);
    assertNotNull(result);
  }

  @Test
  public void patch_disease_log_with_invalid_id_throws_not_found_exception() {
    User user = TestDataProvider.getUser();
    PatchDiseaseLog patch = TestDataProvider.DISEASE_LOG.getPatchDiseaseLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> diseaseLogService.update(INVALID_ID, patch));
  }

  @Test
  public void patch_disease_log_if_not_owner_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog();
    PatchDiseaseLog patch = TestDataProvider.DISEASE_LOG.getPatchDiseaseLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> diseaseLogService.update(VALID_ID, patch));
    assertEquals(OWNER_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void
      patch_disease_log_with_invalid_disease_id_should_bad_request_exception_with_message() {
    User user = TestDataProvider.getUser();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog(user);
    PatchDiseaseLog patch = TestDataProvider.DISEASE_LOG.getPatchDiseaseLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));
    when(diseaseRepository.findById(patch.diseaseId())).thenReturn(Optional.empty());

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> diseaseLogService.update(VALID_ID, patch));
    assertEquals(NON_EXISTENT_DISEASE, exception.getMessage());
  }

  @Test
  public void patch_disease_log_should_return_updated_disease_log() {
    User user = TestDataProvider.getUser();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog(user);
    Disease disease = TestDataProvider.DISEASE.getDisease(user);
    PatchDiseaseLog patch = TestDataProvider.DISEASE_LOG.getPatchDiseaseLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));
    when(diseaseRepository.findById(patch.diseaseId())).thenReturn(Optional.of(disease));

    DiseaseLogResponse result = diseaseLogService.update(VALID_ID, patch);
    assertNotNull(result);

    assertEquals(patch.isHealed(), result.isHealed());
    assertEquals(patch.healedOnDate(), result.healedOnDate());
  }

  @Test
  public void delete_non_existent_disease_log_should_throw_not_found_exception() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> diseaseLogService.delete(VALID_ID));
  }

  @Test
  public void delete_non_disease_by_non_owner_log_should_throw_forbidden_exception_with_message() {
    User user = TestDataProvider.getUser();
    DiseaseLog diseaseLog = TestDataProvider.DISEASE_LOG.getDiseaseLog();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseLogRepository.findById(VALID_ID)).thenReturn(Optional.of(diseaseLog));

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> diseaseLogService.delete(VALID_ID));
    assertEquals(OWNER_OR_ADMIN_ONLY_ACTION, exception.getMessage());
  }
}
