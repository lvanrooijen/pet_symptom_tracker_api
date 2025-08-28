package com.laila.pet_symptom_tracker.entities.breed;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
  List<Breed> findByDeletedFalse();

  Optional<Breed> findByIdAndDeletedFalse(Long id);
}
