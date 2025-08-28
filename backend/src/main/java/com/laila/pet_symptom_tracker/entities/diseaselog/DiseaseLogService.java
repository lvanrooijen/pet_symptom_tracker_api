package com.laila.pet_symptom_tracker.entities.diseaselog;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.*;

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
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiseaseLogService {
  private final DiseaseLogRepository diseaseLogRepository;
  private final DiseaseRepository diseaseRepository;
  private final PetRepository petRepository;
  private final AuthenticationService authenticationService;

  public DiseaseLogResponse create(PostDiseaseLog postBody) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Pet pet =
        petRepository
            .findById(postBody.petId())
            .orElseThrow(() -> new BadRequestException(NON_EXISTENT_PET_TYPE));

    if (pet.isNotOwner(loggedInUser) && loggedInUser.hasUserRole()) {
      throw new ForbiddenException(OWNER_OR_ADMIN_ONLY_ACTION);
    }

    Disease disease =
        diseaseRepository
            .findById(postBody.diseaseId())
            .orElseThrow(() -> new BadRequestException(NON_EXISTENT_DISEASE));

    DiseaseLog createdDiseaseLog =
        DiseaseLog.builder()
            .pet(pet)
            .disease(disease)
            .discoveryDate(
                postBody.discoveryDate() == null ? LocalDate.now() : postBody.discoveryDate())
            .isHealed(false)
            .build();

    diseaseLogRepository.save(createdDiseaseLog);

    return DiseaseLogResponse.from(createdDiseaseLog);
  }

  public List<DiseaseLogResponse> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<DiseaseLog> diseaseLogs = diseaseLogRepository.findAll();

    if (loggedInUser.hasUserRole()) {
      return diseaseLogs.stream()
          .filter(log -> log.getPet().isOwner(loggedInUser))
          .map(DiseaseLogResponse::from)
          .toList();
    }

    return diseaseLogs.stream().map(DiseaseLogResponse::from).toList();
  }

  public DiseaseLogResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    DiseaseLog diseaseLog = diseaseLogRepository.findById(id).orElseThrow(NotFoundException::new);
    if (diseaseLog.getPet().isOwner(loggedInUser)
        || loggedInUser.hasModeratorRole()
        || loggedInUser.hasAdminRole()) {
      return DiseaseLogResponse.from(diseaseLog);
    } else {
      throw new ForbiddenException(OWNER_OR_ADMIN_ONLY_ACTION);
    }
  }

  public DiseaseLogResponse update(Long id, PatchDiseaseLog patch) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    DiseaseLog update = diseaseLogRepository.findById(id).orElseThrow(NotFoundException::new);

    if (update.getPet().isNotOwner(loggedInUser) && loggedInUser.hasUserRole()) {
      throw new ForbiddenException(OWNER_OR_ADMIN_ONLY_ACTION);
    }

    if (patch.isHealed() != null) {
      update.setIsHealed(patch.isHealed());
    }
    if (patch.healedOnDate() != null) {
      update.setHealedOnDate(patch.healedOnDate());
    }
    if (patch.diseaseId() != null) {
      Disease disease =
          diseaseRepository
              .findById(patch.diseaseId())
              .orElseThrow(() -> new BadRequestException(NON_EXISTENT_DISEASE));
      update.setDisease(disease);
    }
    if (patch.discoveryDate() != null) {
      update.setDiscoveryDate(patch.discoveryDate());
    }
    diseaseLogRepository.save(update);
    return DiseaseLogResponse.from(update);
  }

  public void delete(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    DiseaseLog log = diseaseLogRepository.findById(id).orElseThrow(NotFoundException::new);
    if (log.getPet().isNotOwner(loggedInUser) && loggedInUser.hasUserRole()) {
      throw new ForbiddenException(OWNER_OR_ADMIN_ONLY_ACTION);
    }
    diseaseLogRepository.deleteById(id);
  }
}
