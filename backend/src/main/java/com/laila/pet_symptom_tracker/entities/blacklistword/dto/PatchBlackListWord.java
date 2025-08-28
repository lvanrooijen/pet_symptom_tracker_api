package com.laila.pet_symptom_tracker.entities.blacklistword.dto;

public record PatchBlackListWord(String word) {
  public PatchBlackListWord(String word) {
    this.word = word.toLowerCase().trim();
  }
}
