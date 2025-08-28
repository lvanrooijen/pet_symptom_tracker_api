package com.laila.pet_symptom_tracker.entities.blacklistword;

import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.ADMIN_ONLY_ACTION;
import static com.laila.pet_symptom_tracker.exceptions.ExceptionMessages.DUPLICATE_BLACKLIST_WORD;
import static com.laila.pet_symptom_tracker.testdata.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PatchBlackListWord;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PostBlackListWord;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.exceptions.generic.DuplicateValueException;
import com.laila.pet_symptom_tracker.exceptions.generic.ForbiddenException;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.testdata.TestDataProvider;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlackListWordServiceTest {
  @InjectMocks BlackListWordService blackListWordService;
  @Mock private BlackListWordRepository blackListWordRepository;
  @Mock private AuthenticationService authenticationService;

  @Test
  public void user_created_blacklisted_word_throws_forbidden_exception_with_right_message() {
    User user = TestDataProvider.getUser();
    PostBlackListWord requestBody = TestDataProvider.BLACK_LIST_WORD.getPostBlacklistWord();

    when(authenticationService.getAuthenticatedUser()).thenReturn(user);
    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> blackListWordService.create(requestBody));
    assertEquals(ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void moderator_created_blacklisted_word_throws_forbidden_exception_with_right_message() {
    User moderator = TestDataProvider.getModerator();
    PostBlackListWord requestBody = TestDataProvider.BLACK_LIST_WORD.getPostBlacklistWord();

    when(authenticationService.getAuthenticatedUser()).thenReturn(moderator);
    ForbiddenException exception =
        assertThrows(ForbiddenException.class, () -> blackListWordService.create(requestBody));

    assertEquals(ADMIN_ONLY_ACTION, exception.getMessage());
  }

  @Test
  public void admin_created_blacklisted_word_returns_created_word() {
    User admin = TestDataProvider.getAdmin();
    BlackListWord word = TestDataProvider.BLACK_LIST_WORD.getBlackListWord();
    PostBlackListWord postBlackListWord = TestDataProvider.BLACK_LIST_WORD.getPostBlacklistWord();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(blackListWordRepository.findByWordIgnoreCase(word.getWord())).thenReturn(Optional.empty());

    BlackListWord createdWord = blackListWordService.create(postBlackListWord);
    assertEquals(word.getWord(), createdWord.getWord());
  }

  @Test
  public void creating_duplicate_word_throws_duplicate_value_exception_with_correct_message() {
    User admin = TestDataProvider.getAdmin();
    BlackListWord word = TestDataProvider.BLACK_LIST_WORD.getBlackListWord();
    PostBlackListWord postBlackListWord = TestDataProvider.BLACK_LIST_WORD.getPostBlacklistWord();

    when(authenticationService.getAuthenticatedUser()).thenReturn(admin);
    when(blackListWordRepository.findByWordIgnoreCase(word.getWord()))
        .thenReturn(Optional.of(word));

    DuplicateValueException exception =
        assertThrows(
            DuplicateValueException.class, () -> blackListWordService.create(postBlackListWord));

    assertEquals(DUPLICATE_BLACKLIST_WORD, exception.getMessage());
  }

  @Test
  public void get_non_existing_blacklisted_word_by_id_throws_not_found_exception() {

    when(blackListWordRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> blackListWordService.getById(INVALID_ID));
  }

  @Test
  public void update_non_blacklisted_word_with_invalid_id_throws_not_found_exception() {
    PatchBlackListWord postBlackListWord = TestDataProvider.BLACK_LIST_WORD.getPatchBlackListWord();

    when(blackListWordRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> blackListWordService.update(INVALID_ID, postBlackListWord));
  }

  @Test
  public void update_blacklisted_word_works() {
    BlackListWord blackListWord = TestDataProvider.BLACK_LIST_WORD.getBlackListWord();
    PatchBlackListWord patch = TestDataProvider.BLACK_LIST_WORD.getPatchBlackListWord();

    when(blackListWordRepository.findById(VALID_ID)).thenReturn(Optional.of(blackListWord));

    BlackListWord result = blackListWordService.update(VALID_ID, patch);

    assertEquals(patch.word(), result.getWord());
  }

  @Test
  public void delete_with_invalid_id_throws_not_found_exception() {
    when(blackListWordRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> blackListWordService.delete(INVALID_ID));
  }
}
