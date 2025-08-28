package com.laila.pet_symptom_tracker.entities.pettype;

import com.laila.pet_symptom_tracker.entities.user.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE pet_types SET deleted = true WHERE id=?")
@Table(name = "pet_types")
public class PetType {
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

  @JoinColumn(name = "pet_type_creator_id")
  @ManyToOne
  private User creator;

  @Builder
  private PetType(String name, User creator) {
    this.name = name;
    this.creator = creator;
  }

  public PetType(Long id, String name, boolean deleted, User creator) {
    this.id = id;
    this.name = name;
    this.deleted = deleted;
    this.creator = creator;
  }
}
