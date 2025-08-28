package com.laila.pet_symptom_tracker.entities.pettype;

import com.laila.pet_symptom_tracker.entities.pettype.dto.PatchPetType;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PetTypeResponse;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PostPetType;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.PET_TYPES)
public class PetTypeController {
  private final PetTypeService petTypeService;

  @PostMapping
  public ResponseEntity<PetTypeResponse> create(@RequestBody PostPetType postPetType) {
    PetTypeResponse createdPetType = petTypeService.create(postPetType);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdPetType.id())
            .toUri();

    return ResponseEntity.created(location).body(createdPetType);
  }

  @GetMapping
  public ResponseEntity<List<PetTypeResponse>> getAll() {
    List<PetTypeResponse> petTypes = petTypeService.getAll();
    return ResponseEntity.ok(petTypes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PetTypeResponse> getById(@PathVariable Long id) {
    PetTypeResponse petType = petTypeService.getById(id);
    return ResponseEntity.ok(petType);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<PetTypeResponse> update(
      @PathVariable Long id, @RequestBody PatchPetType patch) {
    PetTypeResponse updatedPetType = petTypeService.patch(id, patch);
    return ResponseEntity.ok(updatedPetType);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    petTypeService.delete(id);
    return ResponseEntity.ok().build();
  }
}
