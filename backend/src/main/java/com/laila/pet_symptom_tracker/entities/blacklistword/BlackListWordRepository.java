package com.laila.pet_symptom_tracker.entities.blacklistword;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListWordRepository extends JpaRepository<BlackListWord, Long> {
  Optional<BlackListWord> findByWordIgnoreCase(String word);
}
