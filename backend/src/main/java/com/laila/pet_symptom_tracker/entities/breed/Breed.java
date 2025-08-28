package com.laila.pet_symptom_tracker.entities.breed;

import com.laila.pet_symptom_tracker.entities.pettype.PetType;
import com.laila.pet_symptom_tracker.entities.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE breeds SET deleted = true WHERE id=?")
@Table(name = "breeds")
public class Breed {
  @Column(name = "id")
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, name = "name")
  @Setter
  private String name;

  @Column(nullable = false, name = "deleted")
  @Setter
  private boolean deleted = false;

  @JoinColumn(name = "breed_pet_type_id", nullable = false)
  @Setter
  @ManyToOne
  private PetType petType;

  @JoinColumn(name = "breed_creator_id", nullable = false)
  @ManyToOne
  private User creator;

  @Builder
  private Breed(String name, PetType petType, User creator) {
    this.name = name;
    this.petType = petType;
    this.creator = creator;
  }

  public Breed(Long id, String name, PetType petType, User creator) {
    this.id = id;
    this.name = name;
    this.petType = petType;
    this.creator = creator;
  }
}
