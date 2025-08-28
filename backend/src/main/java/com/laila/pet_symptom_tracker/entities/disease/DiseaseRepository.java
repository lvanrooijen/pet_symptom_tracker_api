package com.laila.pet_symptom_tracker.entities.disease;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
  List<Disease> findByDeletedFalse();
  Optional<Disease> findByIdAndDeletedFalse(Long id);
}
