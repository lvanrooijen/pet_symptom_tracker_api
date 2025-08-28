package com.laila.pet_symptom_tracker.entities.symptom;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE symptoms SET deleted = true WHERE id=?")
@Table(name = "symptoms")
public class Symptom {
  @Column(name = "id")
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, name = "name")
  @Setter
  private String name;

  @Column(nullable = false, name = "description")
  @Setter
  private String description;

  @Column(nullable = false, name = "is_deleted")
  @Setter
  private Boolean deleted = false;

  @Column(nullable = false, name = "is_verified")
  @Setter
  private Boolean verified;

  @Builder
  private Symptom(String name, String description, Boolean verified) {
    this.name = name;
    this.description = description;
    this.verified = verified;
  }

  public Symptom(Long id, String name, String description, Boolean deleted, Boolean verified) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.deleted = deleted;
    this.verified = verified;
  }
}
