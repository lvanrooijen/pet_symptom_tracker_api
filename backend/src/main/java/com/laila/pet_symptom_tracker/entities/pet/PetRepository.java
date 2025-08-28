package com.laila.pet_symptom_tracker.entities.pet;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
  public List<Pet> findByOwnerId(UUID userId);
}
