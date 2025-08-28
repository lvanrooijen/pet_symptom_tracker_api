package com.laila.pet_symptom_tracker.entities.disease;

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
@SQLDelete(sql = "UPDATE diseases SET deleted = true WHERE id=?")
@Table(name = "diseases")
public class Disease {
  @Column(name = "id")
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, name = "name")
  @Setter
  private String name;

  @Column(nullable = true, name = "description")
  @Setter
  private String description;

  @Column(nullable = false, name = "deleted")
  @Setter
  private boolean deleted = false;

  @JoinColumn(name = "disease_creator_id", nullable = false)
  @ManyToOne
  private User creator;

  @Builder
  private Disease(String name, String description, User creator) {
    this.name = name;
    this.description = description;
    this.creator = creator;
  }

  public Disease(Long id, String name, String description, boolean deleted, User creator) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.deleted = deleted;
    this.creator = creator;
  }
}
