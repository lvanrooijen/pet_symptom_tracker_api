package com.laila.pet_symptom_tracker.entities.diseaselog;

import com.laila.pet_symptom_tracker.entities.diseaselog.dto.DiseaseLogResponse;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.PatchDiseaseLog;
import com.laila.pet_symptom_tracker.entities.diseaselog.dto.PostDiseaseLog;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(Routes.DISEASE_LOG)
@RequiredArgsConstructor
public class DiseaseLogController {
  private final DiseaseLogService diseaseLogService;

  @PostMapping
  public ResponseEntity<DiseaseLogResponse> create(@RequestBody @Valid PostDiseaseLog diseaseLog) {
    DiseaseLogResponse createdDiseaseLog = diseaseLogService.create(diseaseLog);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdDiseaseLog.id())
            .toUri();
    return ResponseEntity.created(location).body(createdDiseaseLog);
  }

  @GetMapping
  public ResponseEntity<List<DiseaseLogResponse>> getAll() {
    List<DiseaseLogResponse> logs = diseaseLogService.getAll();

    if (logs.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(logs);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DiseaseLogResponse> getById(@PathVariable Long id) {
    DiseaseLogResponse diseaseLog = diseaseLogService.getById(id);

    return ResponseEntity.ok(diseaseLog);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<DiseaseLogResponse> update(
      @PathVariable Long id, @RequestBody PatchDiseaseLog patch) {
    DiseaseLogResponse updateDiseaseLog = diseaseLogService.update(id, patch);
    return ResponseEntity.ok(updateDiseaseLog);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    diseaseLogService.delete(id);
    return ResponseEntity.ok().build();
  }
}
