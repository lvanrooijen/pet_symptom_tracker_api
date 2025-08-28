package com.laila.pet_symptom_tracker.testdata;

import com.laila.pet_symptom_tracker.entities.blacklistword.BlackListWord;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PatchBlackListWord;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PostBlackListWord;

public class BlackListWordTestData {
  public PostBlackListWord getPostBlacklistWord() {
    return new PostBlackListWord("fuck");
  }

  public PatchBlackListWord getPatchBlackListWord() {
    return new PatchBlackListWord("fudge");
  }

  public BlackListWord getBlackListWord() {
    return new BlackListWord("fuck");
  }
}
