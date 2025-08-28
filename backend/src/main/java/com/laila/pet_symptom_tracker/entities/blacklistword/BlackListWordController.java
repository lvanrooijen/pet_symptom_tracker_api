package com.laila.pet_symptom_tracker.entities.blacklistword;

import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PatchBlackListWord;
import com.laila.pet_symptom_tracker.entities.blacklistword.dto.PostBlackListWord;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.BLACK_LISTED_WORDS)
public class BlackListWordController {
  private final BlackListWordService blackListWordService;

  @PostMapping
  public ResponseEntity<BlackListWord> create(@RequestBody PostBlackListWord word) {
    BlackListWord createdWord = blackListWordService.create(word);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdWord.getId())
            .toUri();

    return ResponseEntity.created(location).body(createdWord);
  }

  @GetMapping
  public List<BlackListWord> getAll() {
    return blackListWordService.getAll();
  }

  @GetMapping("/{id}")
  public BlackListWord getById(@PathVariable Long id) {
    return blackListWordService.getById(id);
  }

  @PatchMapping("/{id}")
  public BlackListWord update(@PathVariable Long id, @RequestBody PatchBlackListWord word) {
    return blackListWordService.update(id, word);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    blackListWordService.delete(id);
    return ResponseEntity.ok().build();
  }
}
