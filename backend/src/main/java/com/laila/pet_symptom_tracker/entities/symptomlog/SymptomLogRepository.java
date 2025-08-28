package com.laila.pet_symptom_tracker.entities.symptomlog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomLogRepository extends JpaRepository<SymptomLog, Long> {
  List<SymptomLog> findByPetId(Long petId);
}
