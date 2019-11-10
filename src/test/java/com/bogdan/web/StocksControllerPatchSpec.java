package com.bogdan.web;

import com.bogdan.repository.TimeMachine;
import com.bogdan.repository.TimeMachineImpl;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static com.bogdan.testdata.Converters.asFloat;
import static com.bogdan.testdata.TestDataParameters.invalidStocksPayloadValuesData;
import static com.bogdan.testdata.TestDataParameters.validStocksPayloadData;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@MicronautTest
class StocksControllerPatchSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  public static Stream<Arguments> missingFields() {
    return Stream.of(
        Arguments.of(
            new JSONObject().toString() // missing name and currentPrice
        ),
        Arguments.of(
            new JSONObject().accumulate("name", "name_1").toString() // missing currentPrice

        ),
        Arguments.of(
            new JSONObject().accumulate("currentPrice", 1.23).toString() // missing name
        )
    );
  }

  private static Stream<Arguments> validPayloadData() {
    return Stream.concat(validStocksPayloadData(), missingFields());
  }

  @ParameterizedTest
  @MethodSource("validPayloadData")
  void testUpdateStock(JSONObject patchPayload) {
    JSONObject createdPayload = stocksApiClient.createStock();
    Instant updateTime = Instant.ofEpochSecond(9876);
    Integer id = createdPayload.getInt("id");
    changeTime(updateTime);

    String expectedName = expectedName(patchPayload, createdPayload, "name");
    Float expectedCurrentPrice = expectedCurrentPrice(patchPayload, createdPayload, "currentPrice");

    given().contentType(APPLICATION_JSON)
        .and()
        .body(patchPayload.toString()).
        when().patch(stocksApi() + "/" + id).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(id)).
        and().body("name", is(expectedName)).
        and().body("currentPrice", is(expectedCurrentPrice)).
        and().body("lastUpdate", is((asFloat(updateTime)))
    );
  }

  private String expectedName(JSONObject patched, JSONObject original, String key) {
    return patched.has(key) ? patched.getString(key) : original.getString(key);
  }

  private Float expectedCurrentPrice(JSONObject patched, JSONObject original, String key) {
    return patched.has(key) ? patched.getFloat(key) : original.getFloat(key);
  }

  @Test
  void testUpdateStockMultipleTimes() {
    JSONObject createdStock = stocksApiClient.createStock();
    int stockId = createdStock.getInt("id");

    JSONObject updatedStockFirstTime = new JSONObject()
        .accumulate("name", "updated_name_first")
        .accumulate("currentPrice", 1.88);
    int firstUpdateTimeSeconds = 7654;

    updateStock(updatedStockFirstTime, stockId, firstUpdateTimeSeconds);

    JSONObject updatedStockSecondTime = new JSONObject()
        .accumulate("name", "updated_name_second")
        .accumulate("currentPrice", 1.99);

    int secondUpdateTimeSeconds = 3456;
    updateStock(updatedStockSecondTime, stockId, secondUpdateTimeSeconds);
  }

  @Test
  void testUpdateNonExistingStock_shouldFail() {
    int nonExistingStockId = 842;
    JSONObject payload = new JSONObject()
        .accumulate("name", "name_" + nonExistingStockId)
        .accumulate("currentPrice", 1.294);

    given().contentType(APPLICATION_JSON)
        .and()
        .body(payload.toString()).
        when().patch(stocksApi() + "/" + nonExistingStockId).
        then().assertThat().statusCode(is(BAD_REQUEST.getCode()));

  }

  private void updateStock(final JSONObject updatedStockPayload, final int id, final int timeInSeconds) {
    Instant updateInstant = Instant.ofEpochSecond(timeInSeconds);
    changeTime(updateInstant);

    given().contentType(APPLICATION_JSON)
        .and()
        .body(updatedStockPayload.toString()).
        when().patch(stocksApi() + "/" + id).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(id)).
        and().body("name", is(updatedStockPayload.get("name"))).
        and().body("currentPrice", is(updatedStockPayload.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(updateInstant))));
  }

  private static Stream<Arguments> invalidPayloadData() {
    return invalidStocksPayloadValuesData();
  }

  @ParameterizedTest
  @MethodSource("invalidPayloadData")
  void testCreateStock_invalidJson(String payload, Matcher<String> matcher) {
    JSONObject createdStock = stocksApiClient.createStock();
    given()
        .contentType(APPLICATION_JSON)
        .and().body(payload)
        .when()
        .patch(stocksApi() + "/" + createdStock.getInt("id"))
        .then().statusCode(is(BAD_REQUEST.getCode()))
        .and().body(matcher);
  }

}