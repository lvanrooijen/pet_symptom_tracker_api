package com.laila.pet_symptom_tracker.entities.symptomlog;

import com.laila.pet_symptom_tracker.entities.pet.Pet;
import com.laila.pet_symptom_tracker.entities.symptom.Symptom;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "symptom_logs")
public class SymptomLog {
  @JoinColumn(name = "symptom_log_pet_id")
  @ManyToOne
  @Setter
  Pet pet;

  @Column(name = "id")
  @Id
  @GeneratedValue
  private Long id;

  @JoinColumn(name = "symptom_log_symptom_id")
  @ManyToOne
  @Setter
  private Symptom symptom;

  @Column(nullable = true, name = "details")
  @Setter
  private String details;

  @Column(nullable = false, name = "report_date")
  @Setter
  private LocalDate reportDate;

  @Builder
  private SymptomLog(Pet pet, Symptom symptom, String details, LocalDate reportDate) {
    this.pet = pet;
    this.symptom = symptom;
    this.details = details;
    this.reportDate = reportDate;
  }

  public SymptomLog(Long id, Pet pet, Symptom symptom, String details, LocalDate reportDate) {
    this.id = id;
    this.pet = pet;
    this.symptom = symptom;
    this.details = details;
    this.reportDate = reportDate;
  }
}
