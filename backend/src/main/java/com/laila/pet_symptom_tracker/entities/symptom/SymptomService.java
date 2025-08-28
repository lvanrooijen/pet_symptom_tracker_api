package com.laila.pet_symptom_tracker.entities.symptom;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_OR_MODERATOR_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.PATCH_DELETED_ENTITY;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PatchSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PostSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.SymptomResponse;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.exceptions.generic.PatchDeletedEntityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SymptomService {
  private final SymptomRepository symptomRepository;
  private final AuthenticationService authenticationService;

  public SymptomResponse create(PostSymptom postSymptom) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Symptom created =
        Symptom.builder()
            .name(postSymptom.name())
            .description(postSymptom.description())
            .verified(loggedInUser.hasAdminRole() || loggedInUser.hasModeratorRole())
            .build();

    symptomRepository.save(created);
    return SymptomResponse.from(created);
  }

  public SymptomResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();

    Symptom symptom;

    if (loggedInUser.hasAdminRole()) {
      symptom = symptomRepository.findById(id).orElseThrow(NotFoundException::new);
    } else {
      symptom = symptomRepository.findByIdAndDeletedFalse(id).orElseThrow(NotFoundException::new);
    }

    return SymptomResponse.from(symptom);
  }

  public List<SymptomResponse> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<Symptom> symptoms;
    if (loggedInUser.hasAdminRole()) {
      symptoms = symptomRepository.findAll();
    } else {
      symptoms = symptomRepository.findByDeletedFalse();
    }
    return symptoms.stream().map(SymptomResponse::from).toList();
  }

  public SymptomResponse update(Long id, PatchSymptom patch) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Symptom symptom = symptomRepository.findById(id).orElseThrow(NotFoundException::new);

    if (symptom.getDeleted()) {
      throw new PatchDeletedEntityException(PATCH_DELETED_ENTITY);
    }

    if (patch.isVerified() != null) {
      if (loggedInUser.hasUserRole()) {
        throw new ForbiddenException(ADMIN_OR_MODERATOR_ONLY_ACTION);
      }
      symptom.setVerified(patch.isVerified());
    }

    if (patch.name() != null) {
      symptom.setName(patch.name());
    }

    if (patch.description() != null) {
      symptom.setDescription(patch.description());
    }

    symptomRepository.save(symptom);
    return SymptomResponse.from(symptom);
  }

  public void delete(Long id) {
    Symptom symptom = symptomRepository.findById(id).orElseThrow(NotFoundException::new);
    symptomRepository.delete(symptom);
  }
}
