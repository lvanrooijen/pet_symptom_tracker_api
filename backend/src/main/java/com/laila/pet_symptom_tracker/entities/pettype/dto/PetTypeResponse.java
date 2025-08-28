package com.laila.pet_symptom_tracker.entities.pettype.dto;

import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import java.util.UUID;

public record PetTypeResponse(Long id, String name, UUID creatorId, Boolean deleted) {
  public static PetTypeResponse from(PetType entity) {
    return new PetTypeResponse(
        entity.getId(), entity.getName(), entity.getCreator().getId(), entity.isDeleted());
  }
}
