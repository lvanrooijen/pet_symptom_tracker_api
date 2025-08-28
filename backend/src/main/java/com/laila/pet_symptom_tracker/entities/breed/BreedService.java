package com.laila.pet_symptom_tracker.entities.breed;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.NON_EXISTENT_PET_TYPE;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.breed.dto.BreedResponse;
import com.laila.pet_symptom_tracker.entities.breed.dto.PatchBreed;
import com.laila.pet_symptom_tracker.entities.breed.dto.PostBreed;
import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import com.laila.pet_symptom_tracker.entities.pettype.PetTypeRepository;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.BadRequestException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreedService {
  private final BreedRepository breedRepository;
  private final PetTypeRepository petTypeRepository;
  private final AuthenticationService authenticationService;

  public BreedResponse create(PostBreed postBreed) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    PetType type =
        petTypeRepository
            .findById(postBreed.petTypeId())
            .orElseThrow(() -> new BadRequestException(NON_EXISTENT_PET_TYPE));
    Breed createdBreed =
        Breed.builder().name(postBreed.name()).petType(type).creator(loggedInUser).build();
    breedRepository.save(createdBreed);
    return BreedResponse.from(createdBreed);
  }

  public List<BreedResponse> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<Breed> breeds;
    if (loggedInUser.hasAdminRole()) {
      breeds = breedRepository.findAll();
    } else {
      breeds = breedRepository.findByDeletedFalse();
    }
    return breeds.stream().map(BreedResponse::from).toList();
  }

  public BreedResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Breed breed;
    if (loggedInUser.hasAdminRole()) {
      breed = breedRepository.findById(id).orElseThrow(NotFoundException::new);
    } else {
      breed = breedRepository.findByIdAndDeletedFalse(id).orElseThrow(NotFoundException::new);
    }

    return BreedResponse.from(breed);
  }

  // TODO bijhouden wie wanneer aanpassing heeft gemaakt
  public BreedResponse updateBreed(Long id, PatchBreed patch) {
    Breed updatedBreed = breedRepository.findById(id).orElseThrow(NotFoundException::new);

    if (patch.name() != null) {
      updatedBreed.setName(patch.name());
    }

    if (patch.petTypeId() != null) {
      PetType type =
          petTypeRepository
              .findById(patch.petTypeId())
              .orElseThrow(() -> new BadRequestException(NON_EXISTENT_PET_TYPE));
      updatedBreed.setPetType(type);
    }

    breedRepository.save(updatedBreed);

    return BreedResponse.from(updatedBreed);
  }

  public void deleteById(Long id) {
    Breed breed = breedRepository.findById(id).orElseThrow(NotFoundException::new);
    breedRepository.delete(breed);
  }
}
