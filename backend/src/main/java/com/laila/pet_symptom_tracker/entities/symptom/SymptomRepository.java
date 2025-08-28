package com.laila.pet_symptom_tracker.entities.symptom;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long> {
  List<Symptom> findByDeletedFalse();

  Optional<Symptom> findByIdAndDeletedFalse(Long id);
}
