package com.laila.pet_symptom_tracker.entities.disease;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_OR_MODERATOR_ONLY_ACTION;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.disease.dto.DiseaseResponse;
import com.laila.pet_symptom_tracker.entities.disease.dto.PatchDisease;
import com.laila.pet_symptom_tracker.entities.disease.dto.PostDisease;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiseaseService {
  private final DiseaseRepository diseaseRepository;
  private final AuthenticationService authenticationService;

  public DiseaseResponse create(PostDisease body) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    if (loggedInUser.hasUserRole()) {
      throw new ForbiddenException(ADMIN_OR_MODERATOR_ONLY_ACTION);
    }
    Disease createdDisease =
        Disease.builder()
            .name(body.name())
            .description(body.description())
            .creator(loggedInUser)
            .build();

    diseaseRepository.save(createdDisease);

    return DiseaseResponse.from(createdDisease);
  }

  public DiseaseResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();

    Disease disease;
    if (loggedInUser.hasAdminRole()) {
      disease = diseaseRepository.findById(id).orElseThrow(NotFoundException::new);
    } else {
      disease = diseaseRepository.findByIdAndDeletedFalse(id).orElseThrow(NotFoundException::new);
    }
    return DiseaseResponse.from(disease);
  }

  public List<DiseaseResponse> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<Disease> diseases;
    if (loggedInUser.hasAdminRole()) {
      diseases = diseaseRepository.findAll();
    } else {
      diseases = diseaseRepository.findByDeletedFalse();
    }
    return diseases.stream().map(DiseaseResponse::from).toList();
  }

  public DiseaseResponse update(Long id, PatchDisease patch) {
    Disease updatedDisease = diseaseRepository.findById(id).orElseThrow(NotFoundException::new);
    if (patch.name() != null) {
      updatedDisease.setName(patch.name());
    }
    if (patch.description() != null) {
      updatedDisease.setDescription(patch.description());
    }
    diseaseRepository.save(updatedDisease);
    return DiseaseResponse.from(updatedDisease);
  }

  public void delete(Long id) {
    Disease disease = diseaseRepository.findById(id).orElseThrow(NotFoundException::new);
    diseaseRepository.delete(disease);
  }
}
