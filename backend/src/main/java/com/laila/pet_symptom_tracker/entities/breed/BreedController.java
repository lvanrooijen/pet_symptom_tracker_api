package com.laila.pet_symptom_tracker.entities.breed;

import com.laila.pet_symptom_tracker.entities.breed.dto.BreedResponse;
import com.laila.pet_symptom_tracker.entities.breed.dto.PatchBreed;
import com.laila.pet_symptom_tracker.entities.breed.dto.PostBreed;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(Routes.BREEDS)
@RequiredArgsConstructor
public class BreedController {
  private final BreedService breedService;

  @PostMapping
  public ResponseEntity<BreedResponse> create(@RequestBody @Valid PostBreed postBreed) {
    BreedResponse breed = breedService.create(postBreed);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(breed.id())
            .toUri();
    return ResponseEntity.created(location).body(breed);
  }

  @GetMapping
  public ResponseEntity<List<BreedResponse>> getAll() {
    List<BreedResponse> breeds = breedService.getAll();
    if (breeds.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(breeds);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BreedResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(breedService.getById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BreedResponse> update(
      @PathVariable Long id, @RequestBody @Valid PatchBreed patch) {
    return ResponseEntity.ok(breedService.updateBreed(id, patch));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    breedService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
