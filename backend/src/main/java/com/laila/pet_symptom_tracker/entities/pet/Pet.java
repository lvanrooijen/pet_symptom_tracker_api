package com.laila.pet_symptom_tracker.entities.pet;

import com.laila.pet_symptom_tracker.entities.breed.Breed;
import com.laila.pet_symptom_tracker.entities.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "pets")
public class Pet {
  @JoinColumn(name = "pet_owner_id")
  @Setter
  @ManyToOne
  User owner;

  @Column(name = "id")
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "name")
  @Setter
  private String name;

  @JoinColumn(name = "pet_breed_id")
  @Setter
  @ManyToOne
  private Breed breed;

  @Column(name = "date_of_birth")
  @Setter
  private LocalDate dateOfBirth;

  @Column(nullable = false, name = "is_alive")
  @Setter
  private Boolean isAlive;

  @Column(nullable = true, name = "date_of_death")
  @Setter
  private LocalDate dateOfDeath;

  @Builder
  private Pet(
      User owner,
      Long id,
      String name,
      Breed breed,
      LocalDate dateOfBirth,
      Boolean isAlive,
      LocalDate dateOfDeath) {
    this.owner = owner;
    this.id = id;
    this.name = name;
    this.breed = breed;
    this.dateOfBirth = dateOfBirth;
    this.isAlive = isAlive;
    this.dateOfDeath = dateOfDeath;
  }

  public Pet(String name, LocalDate dateOfBirth, Boolean isAlive) {
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.isAlive = isAlive;
  }

  public boolean isOwner(User user) {
    return owner.isSameUser(user);
  }

  public boolean isNotOwner(User user) {
    return !owner.isSameUser(user);
  }
}
