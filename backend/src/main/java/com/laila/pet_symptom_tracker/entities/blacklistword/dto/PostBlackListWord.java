package com.laila.pet_symptom_tracker.entities.blacklistword.dto;

public record PostBlackListWord(String word) {
  public PostBlackListWord(String word) {
    this.word = word.toLowerCase().trim();
  }
}
