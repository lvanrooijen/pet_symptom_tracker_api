package com.laila.pet_symptom_tracker.entities.symptom;

import com.laila.pet_symptom_tracker.entities.symptom.dto.SymptomResponse;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PatchSymptom;
import com.laila.pet_symptom_tracker.entities.symptom.dto.PostSymptom;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
@RequestMapping(Routes.SYMPTOM)
public class SymptomController {
  private final SymptomService symptomService;

  @PostMapping
  public ResponseEntity<SymptomResponse> create(@RequestBody PostSymptom postSymptom) {
    SymptomResponse symptom = symptomService.create(postSymptom);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(symptom.id())
            .toUri();
    return ResponseEntity.created(location).body(symptom);
  }

  @GetMapping
  public ResponseEntity<List<SymptomResponse>> getAll() {
    List<SymptomResponse> symptoms = symptomService.getAll();

    return ResponseEntity.ok(symptoms);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SymptomResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(symptomService.getById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<SymptomResponse> update(
      @PathVariable Long id, @RequestBody PatchSymptom patch) {
    SymptomResponse updatedSymptom = symptomService.update(id, patch);
    return ResponseEntity.ok(updatedSymptom);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    symptomService.delete(id);
    return ResponseEntity.ok().build();
  }
}
