package com.laila.pet_symptom_tracker.entities.pettype;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_OR_MODERATOR_ONLY_ACTION;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PatchPetType;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PetTypeResponse;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PostPetType;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.mainconfig.ColoredLogger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetTypeService {
  private final PetTypeRepository petTypeRepository;
  private final AuthenticationService authenticationService;

  public PetTypeResponse create(PostPetType dto) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    ColoredLogger.prettyInPink(loggedInUser.getRole().toString());
    if (!loggedInUser.hasModeratorRole() && !loggedInUser.hasAdminRole())
      throw new ForbiddenException(ADMIN_OR_MODERATOR_ONLY_ACTION);

    PetType createdType = PetType.builder().name(dto.name()).creator(loggedInUser).build();

    petTypeRepository.save(createdType);

    return PetTypeResponse.from(createdType);
  }

  public PetTypeResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();

    PetType petType;
    if (loggedInUser.hasAdminRole()) {
      petType = petTypeRepository.findById(id).orElseThrow(NotFoundException::new);
    } else {
      petType = petTypeRepository.findByIdAndDeletedFalse(id).orElseThrow(NotFoundException::new);
    }
    return PetTypeResponse.from(petType);
  }

  public List<PetTypeResponse> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<PetType> petTypes;
    if (loggedInUser.hasAdminRole()) {
      petTypes = petTypeRepository.findAll();
    } else {
      petTypes = petTypeRepository.findByDeletedFalse();
    }

    return petTypes.stream().map(PetTypeResponse::from).toList();
  }

  // TODO bijhouden wie wanneer aanpassing heeft gemaakt
  public PetTypeResponse patch(Long id, PatchPetType patch) {
    PetType petType = petTypeRepository.findById(id).orElseThrow(NotFoundException::new);

    if (patch.name() != null) {
      petType.setName(patch.name());
    }

    petTypeRepository.save(petType);
    return PetTypeResponse.from(petType);
  }

  public void delete(Long id) {
    PetType petType = petTypeRepository.findById(id).orElseThrow(NotFoundException::new);
    petTypeRepository.delete(petType);
  }
}
