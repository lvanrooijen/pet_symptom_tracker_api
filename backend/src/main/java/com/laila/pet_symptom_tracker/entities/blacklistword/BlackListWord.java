package com.laila.pet_symptom_tracker.entities.blacklistword;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "black_list_words")
public class BlackListWord {
  @Id @GeneratedValue private Long id;

  @Setter
  @Column(unique = true, nullable = false, name = "word")
  private String word;

  public BlackListWord(String word) {
    this.word = word;
  }
}
