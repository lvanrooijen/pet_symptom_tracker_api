package com.laila.pet_symptom_tracker.entities.blacklistword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import com.laila.pet_symptom_tracker.mainconfig.Routes;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BlackListWordControllerTest {
  @BeforeAll
  static void initSetUp() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }

  /**
   * Fetch JWT-token for given username (email address).
   *
   * <p>Method sends login to endpoint {@code /api/v1/login}
   *
   * <p>This method uses the standard password ({@code Password123!}). succesful login returns token
   *
   * @param username user email address
   * @return JWT token
   * @throws AssertionError if failed login
   */
  String getToken(String username) {
    String userPayload =
        String.format(
            """
            {
            "username": "%s",
                "password":"Password123!"
                }
            """,
            username);

    return given()
        .contentType("application/json")
        .body(userPayload)
        .when()
        .post("/api/v1/login")
        .then()
        .statusCode(200)
        .extract()
        .path("token");
  }

  @Test
  public void shouldReturnOkWhenPost() {
    String payload =
        """
            {
            "word":"fudgy"
            }
        """;

    Response response =
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + getToken("admin@gmail.com"))
            .body(payload)
            .when()
            .post(Routes.BLACK_LISTED_WORDS)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("word", equalTo("fudgy"))
            .extract()
            .response();

    // delete word after test
    int id = response.path("id");
    given()
        .header("Authorization", "Bearer " + getToken("admin@gmail.com"))
        .when()
        .delete(Routes.BLACK_LISTED_WORDS + "/" + id)
        .then()
        .statusCode(200);
  }

  @Test
  public void shouldReturnOkWithListOfBlackListedWords() {
    Response response =
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + getToken("admin@gmail.com"))
            .when()
            .get(Routes.BLACK_LISTED_WORDS)
            .then()
            .body(notNullValue())
            .statusCode(200)
            .extract()
            .response();
    System.out.println(response);
  }
}
