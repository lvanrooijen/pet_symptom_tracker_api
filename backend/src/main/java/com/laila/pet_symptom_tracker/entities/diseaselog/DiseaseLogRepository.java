package com.laila.pet_symptom_tracker.entities.diseaselog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseLogRepository extends JpaRepository<DiseaseLog, Long> {}
