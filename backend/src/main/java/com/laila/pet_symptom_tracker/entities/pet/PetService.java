package com.laila.pet_symptom_tracker.entities.pet;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.*;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.breed.Breed;
import com.laila.pet_symptom_tracker.entities.breed.BreedRepository;
import com.laila.pet_symptom_tracker.entities.pet.dto.PatchPet;
import com.laila.pet_symptom_tracker.entities.pet.dto.PetResponse;
import com.laila.pet_symptom_tracker.entities.pet.dto.PostPet;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.entities.user.UserRepository;
import com.laila.pet_symptom_tracker.exceptions.generic.BadRequestException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NoContentException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {
  private final PetRepository petRepository;
  private final BreedRepository breedRepository;
  private final UserRepository userRepository;
  private final AuthenticationService authenticationService;

  public PetResponse create(PostPet dto) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Breed breed =
        breedRepository
            .findById(dto.breedId())
            .orElseThrow(() -> new BadRequestException(NON_EXISTENT_BREED));

    Pet pet =
        Pet.builder()
            .name(dto.name())
            .owner(loggedInUser)
            .dateOfBirth(dto.dateOfBirth())
            .breed(breed)
            .isAlive(true)
            .build();

    petRepository.save(pet);

    return PetResponse.from(pet);
  }

  public PetResponse getById(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Pet pet = petRepository.findById(id).orElseThrow(NotFoundException::new);
    if (pet.isOwner(loggedInUser)
        || loggedInUser.hasAdminRole()
        || loggedInUser.hasModeratorRole()) {
      return PetResponse.from(pet);
    } else {
      throw new ForbiddenException(OWNER_OR_MODERATOR_OR_ADMIN_ONLY_ACTION);
    }
  }

  public List<PetResponse> getAll() {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    List<Pet> pets;

    if (loggedInUser.hasAdminRole() || loggedInUser.hasModeratorRole()) {
      pets = petRepository.findAll();
    } else {
      pets = petRepository.findByOwnerId(loggedInUser.getId());
    }

    if (pets.isEmpty()) throw new NoContentException();

    return pets.stream().map(PetResponse::from).toList();
  }

  public PetResponse update(Long id, PatchPet patch) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Pet updatedPet = petRepository.findById(id).orElseThrow(NotFoundException::new);

    if (updatedPet.isNotOwner(loggedInUser)) {
      throw new ForbiddenException(OWNER_OR_ADMIN_ONLY_ACTION);
    }

    if (patch.userId() != null) {
      User newOwner =
          userRepository
              .findById(patch.userId())
              .orElseThrow(
                  () ->
                      new BadRequestException("Can not transfer ownership to non-existent user."));
      updatedPet.setOwner(newOwner);
    }
    if (patch.name() != null) {
      updatedPet.setName(patch.name());
    }
    if (patch.dateOfBirth() != null) {
      updatedPet.setDateOfBirth(patch.dateOfBirth());
    }
    if (patch.isAlive() != null) {
      updatedPet.setIsAlive(patch.isAlive());
    }
    if (patch.dateOfDeath() != null) {
      updatedPet.setDateOfDeath(patch.dateOfDeath());
    }

    if (patch.breedId() != null) {
      Breed breed =
          breedRepository
              .findById(patch.breedId())
              .orElseThrow(() -> new BadRequestException(NON_EXISTENT_BREED));
      updatedPet.setBreed(breed);
    }

    petRepository.save(updatedPet);
    return PetResponse.from(updatedPet);
  }

  public void delete(Long id) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    Pet pet = petRepository.findById(id).orElseThrow(NotFoundException::new);

    if (pet.isOwner(loggedInUser) || loggedInUser.hasAdminRole()) {
      petRepository.deleteById(id);
    } else {
      throw new ForbiddenException(OWNER_OR_ADMIN_ONLY_ACTION);
    }
  }
}
