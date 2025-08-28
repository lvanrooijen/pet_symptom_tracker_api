package com.laila.pet_symptom_tracker.entities.symptomlog;

import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PatchSymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.PostSymptomLog;
import com.laila.pet_symptom_tracker.entities.symptomlog.dto.SymptomLogResponse;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(Routes.SYMPTOM_LOG)
@RequiredArgsConstructor
public class SymptomLogController {
  private final SymptomLogService symptomLogService;

  @PostMapping
  public ResponseEntity<SymptomLogResponse> create(@RequestBody @Valid PostSymptomLog symptom) {
    SymptomLogResponse createdSymptom = symptomLogService.create(symptom);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdSymptom.id())
            .toUri();

    return ResponseEntity.created(location).body(createdSymptom);
  }

  // TODO voeg documentatie toe aan alle endpoints!
  @Operation(
      summary =
          "Returns a List of Symptom logs that connected to user, if an admin calls this endpoint all symptom logs are returned")
  @GetMapping
  public ResponseEntity<List<SymptomLogResponse>> getAll() {
    List<SymptomLogResponse> symptomLogs = symptomLogService.findAll();

    if (symptomLogs.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(symptomLogs);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SymptomLogResponse> getById(@PathVariable Long id) {
    SymptomLogResponse symptomLog = symptomLogService.getById(id);
    return ResponseEntity.ok(symptomLog);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<SymptomLogResponse> update(
      @PathVariable Long id, @RequestBody PatchSymptomLog patch) {
    SymptomLogResponse updatedSymptomLog = symptomLogService.update(id, patch);
    return ResponseEntity.ok(updatedSymptomLog);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    symptomLogService.delete(id);
    return ResponseEntity.ok().build();
  }
}
