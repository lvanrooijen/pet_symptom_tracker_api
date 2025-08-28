package com.laila.pet_symptom_tracker.entities.diseaselog;

import com.laila.pet_symptom_tracker.entities.disease.Disease;
import com.laila.pet_symptom_tracker.entities.pet.Pet;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "disease_logs")
public class DiseaseLog {
  @Column(name = "id")
  @Id
  @GeneratedValue
  private Long id;

  @JoinColumn(name = "disease_log_disease_id")
  @ManyToOne
  @Setter
  private Disease disease;

  @JoinColumn(name = "disease_log_pet_id", nullable = false)
  @ManyToOne
  private Pet pet;

  @Column(nullable = false, name = "discovery_date")
  @Setter
  private LocalDate discoveryDate;

  @Column(nullable = true, name = "healed_on_date")
  @Setter
  private LocalDate healedOnDate;

  @Column(nullable = false, name = "is_healed")
  @Setter
  private Boolean isHealed;

  @Builder
  private DiseaseLog(
      Disease disease, Pet pet, LocalDate discoveryDate, LocalDate healedOnDate, Boolean isHealed) {
    this.disease = disease;
    this.pet = pet;
    this.discoveryDate = discoveryDate;
    this.healedOnDate = healedOnDate;
    this.isHealed = isHealed;
  }
}
