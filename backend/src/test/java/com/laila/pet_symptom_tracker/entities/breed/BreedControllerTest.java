package com.laila.pet_symptom_tracker.entities.breed;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laila.pet_symptom_tracker.entities.authentication.AuthenticationService;
import com.laila.pet_symptom_tracker.entities.breed.dto.BreedResponse;
import com.laila.pet_symptom_tracker.entities.breed.dto.PatchBreed;
import com.laila.pet_symptom_tracker.entities.breed.dto.PostBreed;
import com.laila.pet_symptom_tracker.entities.pettype.dto.PetTypeCompactResponse;
import com.laila.pet_symptom_tracker.entities.user.UserService;
import com.laila.pet_symptom_tracker.exceptions.generic.NotFoundException;
import com.laila.pet_symptom_tracker.mainconfig.Routes;
import com.laila.pet_symptom_tracker.securityconfig.JwtAuthFilter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BreedController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BreedControllerTest {
  PostBreed postBreed;
  BreedResponse breedResponse;
  PetTypeCompactResponse petType;
  List<BreedResponse> breeds;
  Long breedId;
  UUID creatorId;
  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private BreedService breedService;
  @MockitoBean private UserService userService;
  @MockitoBean private JwtAuthFilter jwtAuthFilter;
  @MockitoBean private Authentication authentication;
  @MockitoBean private AuthenticationService authenticationService;

  @BeforeEach
  public void init() {
    breedId = 1L;
    creatorId = UUID.randomUUID();
    postBreed = new PostBreed("Siamese", 1L);
    breedResponse =
        new BreedResponse(1L, "Siamese", new PetTypeCompactResponse(1L, "Cat"), creatorId);
    petType = new PetTypeCompactResponse(1L, "Cat");
    breeds =
        List.of(
            new BreedResponse(1L, "Siamese", new PetTypeCompactResponse(1L, "Cat"), creatorId),
            new BreedResponse(2L, "Rotweiler", new PetTypeCompactResponse(2L, "Dog"), creatorId),
            new BreedResponse(
                3L, "Flemish Giant", new PetTypeCompactResponse(3L, "Rabbit"), creatorId),
            new BreedResponse(4L, "Parrot", new PetTypeCompactResponse(4L, "Bird"), creatorId));
  }

  @Test
  void post_breed_should_execute_in_3_seconds() throws InterruptedException, Exception {
    given(breedService.create(postBreed)).willReturn(breedResponse);

    assertTimeoutPreemptively(
        Duration.ofSeconds(3),
        () ->
            mvc.perform(
                post(Routes.BREEDS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postBreed))));
  }

  @Test
  void successful_post_breed_should_return_201() throws Exception {
    given(breedService.create(postBreed)).willReturn(breedResponse);

    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postBreed)));

    response.andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void create_breed_should_return_get_breed_dto() throws Exception {
    given(breedService.create(ArgumentMatchers.any())).willReturn(breedResponse);

    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postBreed)));

    response
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(breedResponse.id().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(breedResponse.name())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.petType.name", CoreMatchers.is(breedResponse.petType().name())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.petType.id", CoreMatchers.is(breedResponse.id().intValue())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.creatorId", CoreMatchers.is(breedResponse.creatorId().toString())));
  }

  @Test
  void create_breed_without_request_body_should_return_status_code_400() throws Exception {
    given(breedService.create(postBreed)).willReturn(breedResponse);

    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void create_breed_with_blank_name_should_return_status_code_400() throws Exception {
    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PostBreed("", 1L))));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void create_breed_with_name_that_has_less_then_3_characters_should_return_status_code_400()
      throws Exception {
    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PostBreed("ca", 1L))));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void create_breed_with_name_that_has_more_then_30_characters_should_return_status_code_400()
      throws Exception {
    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new PostBreed("catcatcatcatcatcatcatcatcatcatcat", 1L))));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void create_breed_with_missing_name_should_return_status_code_400() throws Exception {
    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PostBreed(null, 1L))));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void create_breed_with_missing_pet_type_id_should_return_status_code_400() throws Exception {
    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PostBreed("Cat", null))));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void create_breed_with_missing_pet_type_id_not_a_number_should_return_status_code_400()
      throws Exception {
    String invalidJson =
        """
        {
            "name": "Cat",
            "petTypeId": "He"
        }
        """;

    ResultActions response =
        mvc.perform(
            post(Routes.BREEDS).contentType(MediaType.APPLICATION_JSON).content(invalidJson));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void get_all_should_return_all_available_breeds() throws Exception {
    when(breedService.getAll()).thenReturn(breeds);

    ResultActions response =
        mvc.perform(get(Routes.BREEDS).contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(breeds.size())));
  }

  @Test
  void get_all_should_return_status_200() throws Exception {
    when(breedService.getAll()).thenReturn(breeds);

    ResultActions response =
        mvc.perform(get(Routes.BREEDS).contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void get_all_should_return_status_204_when_no_content() throws Exception {
    when(breedService.getAll()).thenReturn(new ArrayList<>());

    ResultActions response =
        mvc.perform(get(Routes.BREEDS).contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  void get_by_id_should_return_getBreed() throws Exception {
    when(breedService.getById(1L)).thenReturn(breedResponse);

    mvc.perform(get(Routes.BREEDS + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Siamese"))
        .andExpect(jsonPath("$.petType.id").value(1L))
        .andExpect(jsonPath("$.petType.name").value("Cat"))
        .andExpect(jsonPath("$.creatorId").value(creatorId.toString()));
  }

  @Test
  void get_non_existent_breed_by_id_should_return_status_code_404() throws Exception {
    when(breedService.getById(breedId)).thenThrow(new NotFoundException());

    mvc.perform(get(Routes.BREEDS + "/" + breedId.intValue())).andExpect(status().isNotFound());
  }

  @Test
  public void patch_breed_should_return_patched_breed() throws Exception {
    PatchBreed patch = new PatchBreed("Changed", null);
    BreedResponse patchedBreed = new BreedResponse(breedId, "Changed", petType, creatorId);

    when(breedService.updateBreed(breedId, patch)).thenReturn(patchedBreed);

    ResultActions response =
        mvc.perform(
            patch(Routes.BREEDS + "/" + breedId.intValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)));

    response
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.id").value(patchedBreed.id().intValue()))
        .andExpect(jsonPath("$.name").value(patch.name()))
        .andExpect(jsonPath("$.petType.id").value(patchedBreed.petType().id().intValue()))
        .andExpect(jsonPath("$.petType.name").value(patchedBreed.petType().name()))
        .andExpect(jsonPath("$.creatorId").value(patchedBreed.creatorId().toString()));
  }

  @Test
  public void patch_breed_should_return_status_code_200() throws Exception {
    PatchBreed patch = new PatchBreed("Changed", null);
    BreedResponse patchedBreed = new BreedResponse(breedId, "Changed", petType, creatorId);

    when(breedService.updateBreed(breedId, patch)).thenReturn(patchedBreed);

    ResultActions response =
        mvc.perform(
            patch(Routes.BREEDS + "/" + breedId.intValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)));

    response.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void patch_breed_with_empty_request_body_should_return_status_code_400() throws Exception {
    ResultActions response =
        mvc.perform(
            patch(Routes.BREEDS + "/" + breedId.intValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

    response.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void delete_breed_should_return_status_code_200() throws Exception {
    doNothing().when(breedService).deleteById(breedId);

    ResultActions response =
        mvc.perform(
            delete(Routes.BREEDS + "/" + breedId.intValue())
                .contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void delete_non_existing_breed_should_return_status_code_404() throws Exception {
    doThrow(new NotFoundException()).when(breedService).deleteById(breedId);

    ResultActions response =
        mvc.perform(
            delete(Routes.BREEDS + "/" + breedId.intValue())
                .contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
