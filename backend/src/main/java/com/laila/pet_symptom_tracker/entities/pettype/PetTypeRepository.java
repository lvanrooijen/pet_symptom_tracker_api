package com.laila.pet_symptom_tracker.entities.pettype;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
  List<PetType> findByDeletedFalse();

  Optional<PetType> findByIdAndDeletedFalse(Long id);
}
