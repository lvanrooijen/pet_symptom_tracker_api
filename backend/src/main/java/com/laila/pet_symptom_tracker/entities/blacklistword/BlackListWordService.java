package com.laila.pet_symptom_tracker.entities.blacklistword;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.DUPLICATE_BLACKLIST_WORD;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PatchBlackListWord;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PostBlackListWord;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.DuplicateValueException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListWordService {
  private final BlackListWordRepository blackListWordRepository;
  private final AuthenticationService authenticationService;

  public BlackListWord create(PostBlackListWord requestBody) {
    User loggedInUser = authenticationService.getAuthenticatedUser();
    if (!loggedInUser.hasAdminRole()) {
      throw new ForbiddenException(ADMIN_ONLY_ACTION);
    }

    BlackListWord createdWord = new BlackListWord(requestBody.word());

    if (blackListWordRepository.findByWordIgnoreCase(requestBody.word()).isPresent()) {
      throw new DuplicateValueException(DUPLICATE_BLACKLIST_WORD);
    }

    blackListWordRepository.save(createdWord);
    return createdWord;
  }

  public List<BlackListWord> getAll() {
    return blackListWordRepository.findAll();
  }

  public BlackListWord getById(Long id) {
    return blackListWordRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  public BlackListWord update(Long id, PatchBlackListWord patch) {
    BlackListWord word = blackListWordRepository.findById(id).orElseThrow(NotFoundException::new);

    if (patch.word() != null) {
      word.setWord(patch.word());
    }

    blackListWordRepository.save(word);

    return word;
  }

  public void delete(Long id) {
    blackListWordRepository.findById(id).orElseThrow(NotFoundException::new);
    blackListWordRepository.deleteById(id);
  }
}
