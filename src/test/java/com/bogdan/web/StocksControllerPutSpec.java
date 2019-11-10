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
import static com.bogdan.testdata.TestDataParameters.invalidStocksPayloadData;
import static com.bogdan.testdata.TestDataParameters.validStocksPayloadData;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@MicronautTest
class StocksControllerPutSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  private static Stream<Arguments> validPayloadData() {
    return validStocksPayloadData();
  }

  @ParameterizedTest
  @MethodSource("validPayloadData")
  void testUpdateStock(JSONObject payload) {
    JSONObject createdStock = stocksApiClient.createStock();
    Instant updateTime = Instant.ofEpochSecond(9876);
    Integer id = createdStock.getInt("id");
    changeTime(updateTime);
    given().contentType(APPLICATION_JSON)
        .and()
        .body(payload.toString()).
        when().put(stocksApi() + "/" + id).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(id)).
        and().body("name", is(payload.get("name"))).
        and().body("currentPrice", is(payload.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(updateTime)))
    );
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
  void testUpdateNonExistingStock_shouldCreate() {
    int nonExistingStockId = 842;
    JSONObject payload = new JSONObject()
        .accumulate("name", "name_" + nonExistingStockId)
        .accumulate("currentPrice", 1.294);

    given().contentType(APPLICATION_JSON)
        .and()
        .body(payload.toString()).
        when().put(stocksApi() + "/" + nonExistingStockId).
        then().assertThat().statusCode(is(BAD_REQUEST.getCode()));

  }

  private void updateStock(final JSONObject updatedStockPayload, final int id, final int timeInSeconds) {
    Instant updateInstant = Instant.ofEpochSecond(timeInSeconds);
    changeTime(updateInstant);

    given().contentType(APPLICATION_JSON)
        .and()
        .body(updatedStockPayload.toString()).
        when().put(stocksApi() + "/" + id).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(id)).
        and().body("name", is(updatedStockPayload.get("name"))).
        and().body("currentPrice", is(updatedStockPayload.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(updateInstant))));
  }

  private static Stream<Arguments> invalidPayloadData() {
    return invalidStocksPayloadData();
  }

  @ParameterizedTest
  @MethodSource("invalidPayloadData")
  void testCreateStock_invalidJson(String payload, Matcher<String> matcher) {
    JSONObject createdStock = stocksApiClient.createStock();
    given()
        .contentType(APPLICATION_JSON)
        .and().body(payload)
        .when()
        .put(stocksApi() + "/" + createdStock.getInt("id"))
        .then().statusCode(is(BAD_REQUEST.getCode()))
        .and().body(matcher);
  }

}