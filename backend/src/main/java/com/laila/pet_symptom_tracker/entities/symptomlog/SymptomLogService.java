package com.laila.pet_symptom_tracker.entities.symptomlog;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.*;

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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SymptomLogService {
  private final SymptomLogRepository symptomLogRepository;
  private final SymptomRepository symptomRepository;
  private final PetRepository petRepository;
  private final AuthenticationService authenticationService;

  public SymptomLogResponse create(PostSymptomLog postBody) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Pet pet =
        petRepository
            .findById(postBody.petId())
            .orElseThrow(() -> new BadRequestException(NON_EXISTENT_PET));

    if (pet.isNotOwner(loggedInUser)) {
      throw new ForbiddenException(OWNER_ONLY_ACTION);
    }

    Symptom symptom =
        symptomRepository
            .findById(postBody.symptomId())
            .orElseThrow(() -> new BadRequestException(NON_EXISTENT_SYMPTOM));

    SymptomLog createdSymptomLog =
        SymptomLog.builder()
            .pet(pet)
            .symptom(symptom)
            .details(postBody.details())
            .reportDate(postBody.reportDate())
            .build();

    symptomLogRepository.save(createdSymptomLog);
    return SymptomLogResponse.from(createdSymptomLog);
  }

  public SymptomLogResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    SymptomLog log = symptomLogRepository.findById(id).orElseThrow(NotFoundException::new);
    Pet pet = log.pet;

    if (pet.isOwner(loggedInUser)
        || loggedInUser.hasAdminRole()
        || loggedInUser.hasModeratorRole()) {
      return SymptomLogResponse.from(log);

    } else {
      throw new ForbiddenException(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION);
    }
  }

  public List<SymptomLogResponse> findAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<SymptomLog> symptomLogs = symptomLogRepository.findAll();

    if (loggedInUser.hasUserRole()) {
      return symptomLogs.stream()
          .filter(log -> log.pet.isOwner(loggedInUser))
          .map(SymptomLogResponse::from)
          .toList();
    }

    return symptomLogs.stream().map(SymptomLogResponse::from).toList();
  }

  public SymptomLogResponse update(Long id, PatchSymptomLog patch) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    SymptomLog updatedLog = symptomLogRepository.findById(id).orElseThrow(NotFoundException::new);

    if (patch.details() != null) {
      updatedLog.setDetails(patch.details());
    }
    if (patch.petId() != null) {
      Pet pet =
          petRepository
              .findById(patch.petId())
              .orElseThrow(() -> new BadRequestException(NON_EXISTENT_PET));

      if (pet.isNotOwner(loggedInUser) && loggedInUser.hasUserRole()) {
        throw new ForbiddenException(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION);
      }
      updatedLog.setPet(pet);
    }
    if (patch.symptomId() != null) {
      Symptom symptom =
          symptomRepository
              .findById(patch.symptomId())
              .orElseThrow(() -> new BadRequestException(NON_EXISTENT_SYMPTOM));
      updatedLog.setSymptom(symptom);
    }

    symptomLogRepository.save(updatedLog);

    return SymptomLogResponse.from(updatedLog);
  }

  public void delete(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    SymptomLog log = symptomLogRepository.findById(id).orElseThrow(NotFoundException::new);
    if (log.pet.isNotOwner(loggedInUser) && loggedInUser.hasUserRole())
      throw new ForbiddenException(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION);

    symptomLogRepository.deleteById(id);
  }
}
