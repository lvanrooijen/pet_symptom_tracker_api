package com.laila.pet_symptom_tracker.entities.disease;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_OR_MODERATOR_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.INVALID_ID;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.VALID_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.disease.dto.DiseaseResponse;
import com.laila.pet_symptom_tracker.entities.disease.dto.PatchDisease;
import com.laila.pet_symptom_tracker.entities.disease.dto.PostDisease;
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
class DiseaseServiceTest {
  @Mock DiseaseRepository diseaseRepository;
  @Mock AuthenticationService authenticationService;
  @InjectMocks DiseaseService diseaseService;

  @Test
  public void create_disease_by_user_should_throw_forbidden_exception_with_correct_message() {
    User user = TestDataProvider.getUser();
    PostDisease postDisease = TestDataProvider.DISEASE.getPostDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> diseaseService.create(postDisease));

    assertEquals(ADMIN_OR_MODERATOR_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void mod_can_create_disease() {
    User moderator = TestDataProvider.getModerator();
    PostDisease requestBody = TestDataProvider.DISEASE.getPostDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    DiseaseResponse result = diseaseService.create(requestBody);

    assertNotNull(result);
    verify(diseaseRepository).save(any(Disease.class));
  }

  @Test
  public void admin_can_create_disease() {
    User admin = TestDataProvider.getAdmin();
    PostDisease requestBody = TestDataProvider.DISEASE.getPostDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    DiseaseResponse result = diseaseService.create(requestBody);

    assertNotNull(result);
    verify(diseaseRepository).save(any(Disease.class));
  }

  @Test
  public void create_disease_returns_right_values() {
    User admin = TestDataProvider.getAdmin();
    PostDisease requestBody = TestDataProvider.DISEASE.getPostDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    DiseaseResponse result = diseaseService.create(requestBody);

    assertEquals(requestBody.name(), result.name());
    assertEquals(requestBody.description(), result.description());
    verify(diseaseRepository).save(any(Disease.class));
  }

  @Test
  public void get_all_as_admin_should_show_soft_deleted_diseases() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);

    diseaseService.getAll();

    verify(diseaseRepository).findAll();
  }

  @Test
  public void get_all_as_user_should_not_show_soft_deleted_diseases() {
    User user = TestDataProvider.getUser();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);

    diseaseService.getAll();

    verify(diseaseRepository).findByDeletedFalse();
  }

  @Test
  public void get_disease_with_invalid_id_should_throw_not_found_exception() {
    User admin = TestDataProvider.getAdmin();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(diseaseRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> diseaseService.getById(INVALID_ID));
  }

  @Test
  public void get_by_id_as_user_should_not_return_soft_deleted_diseases() {
    User user = TestDataProvider.getUser();
    Disease disease = TestDataProvider.DISEASE.getDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    when(diseaseRepository.findByIdAndDeletedFalse(VALID_ID)).thenReturn(Optional.of(disease));

    diseaseService.getById(VALID_ID);
    verify(diseaseRepository).findByIdAndDeletedFalse(VALID_ID);
  }

  @Test
  public void get_by_id_as_admin_should_return_soft_deleted_diseases() {
    User admin = TestDataProvider.getAdmin();
    Disease disease = TestDataProvider.DISEASE.getDisease();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(diseaseRepository.findById(VALID_ID)).thenReturn(Optional.of(disease));

    diseaseService.getById(VALID_ID);
    verify(diseaseRepository).findById(VALID_ID);
  }

  @Test
  public void patch_disease_with_invalid_id_should_throw_not_found_exception() {
    PatchDisease patch = TestDataProvider.DISEASE.getPatchDisease();

    when(diseaseRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> diseaseService.update(INVALID_ID, patch));
  }

  @Test
  public void patch_disease_should_return_patched_disease() {
    Disease disease = TestDataProvider.DISEASE.getDisease();
    PatchDisease patch = TestDataProvider.DISEASE.getPatchDisease();

    when(diseaseRepository.findById(VALID_ID)).thenReturn(Optional.of(disease));
    DiseaseResponse result = diseaseService.update(VALID_ID, patch);
    assertEquals(patch.name(), result.name());
    assertEquals(patch.description(), result.description());
  }

  @Test
  public void delete_disease_with_invalid_id_should_throw_not_found_exception() {
    when(diseaseRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> diseaseService.delete(INVALID_ID));
  }
}
