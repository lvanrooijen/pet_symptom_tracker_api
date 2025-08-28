package com.laila.pet_symptom_tracker.entities.blacklistword;

import com.laila.pet_symptom_tracker.mainconfig.ColoredLogger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfanityFilterService {
  private final BlackListWordRepository blackListWordRepository;

  // TODO bijbehorende testclass maken!
  public Boolean violatesProfanityFilter(String word) {
    List<BlackListWord> blackListedWords = blackListWordRepository.findAll();
    boolean test =
        blackListedWords.stream()
            .anyMatch(blackListWord -> blackListWord.getWord().contains(word.toLowerCase()));
    ColoredLogger.logInBlue("Test " + test);

    return blackListedWords.stream()
        .anyMatch(blackListWord -> blackListWord.getWord().contains(word.toLowerCase()));
  }
}
