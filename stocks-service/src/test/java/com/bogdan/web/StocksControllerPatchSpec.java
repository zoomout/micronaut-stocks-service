package com.bogdan.web;

import com.bogdan.events.StocksEventClient;
import com.bogdan.events.StocksEventListener;
import com.bogdan.time.TimeMachine;
import com.bogdan.time.TimeMachineImpl;
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
import static com.bogdan.testdata.SharedTestDataParameters.invalidStocksPayloadValuesData;
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

  @MockBean(StocksEventClient.class)
  public StocksEventClient stocksEventClient() {
    return  stocksEventClient;
  }

  @MockBean(StocksEventListener.class)
  public StocksEventListener stocksEventListener() {
    return  stocksEventListener;
  }

  private static Stream<Arguments> validPayloadData() {
    return Stream.of(
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_1") // should ignore
                .accumulate("currentPrice", 1.23)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("id", 1234) // should ignore a number
                .accumulate("currentPrice", 1.24)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("id", "abc") // should ignore a string
                .accumulate("currentPrice", 1.25)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("currentPrice", 1.2)
                .accumulate("lastUpdate", 1.123) // should ignore a number
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("currentPrice", 1.2)
                .accumulate("lastUpdate", "abc") // should ignore a string
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_4")
                .accumulate("currentPrice", 1.25)
                .accumulate("unknown", "someValue") // should ignore
        ),
        Arguments.of(
            new JSONObject().toString() // should ignore missing name and currentPrice
        ),
        Arguments.of(
            new JSONObject().accumulate("name", "name_1").toString() // should ignore missing currentPrice

        ),
        Arguments.of(
            new JSONObject().accumulate("currentPrice", 1.23).toString() // should ignore missing name
        )
    );
  }

  @ParameterizedTest
  @MethodSource("validPayloadData")
  void testUpdateStock(JSONObject patchPayload) {
    JSONObject createdPayload = stocksApiClient.createStock();
    Instant updateTime = Instant.ofEpochSecond(9876);
    Integer id = createdPayload.getInt("id");
    changeTime(updateTime);

    Float expectedCurrentPrice = expectedCurrentPrice(patchPayload, createdPayload, "currentPrice");

    given().contentType(APPLICATION_JSON)
        .and()
        .body(patchPayload.toString()).
        when().patch(stocksApi() + "/" + id).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(id)).
        and().body("name", is(createdPayload.getString("name"))).
        and().body("currentPrice", is(expectedCurrentPrice)).
        and().body("lastUpdate", is((asFloat(updateTime)))
    );
  }

  private Float expectedCurrentPrice(JSONObject patched, JSONObject original, String key) {
    return patched.has(key) ? patched.getFloat(key) : original.getFloat(key);
  }

  @Test
  void testUpdateStockMultipleTimes() {
    JSONObject createdStock = stocksApiClient.createStock();

    JSONObject updatedStockFirstTime = new JSONObject().accumulate("currentPrice", 1.88);
    int firstUpdateTimeSeconds = 7654;

    updateStockAndVerify(updatedStockFirstTime, createdStock, firstUpdateTimeSeconds);

    JSONObject updatedStockSecondTime = new JSONObject().accumulate("currentPrice", 1.99);

    int secondUpdateTimeSeconds = 3456;
    updateStockAndVerify(updatedStockSecondTime, createdStock, secondUpdateTimeSeconds);
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

  private void updateStockAndVerify(
      final JSONObject updatedStockPayload,
      final JSONObject originalStock,
      final int timeInSeconds
  ) {
    Instant updateInstant = Instant.ofEpochSecond(timeInSeconds);
    changeTime(updateInstant);
    int id = originalStock.getInt("id");
    String name = originalStock.getString("name");
    given().contentType(APPLICATION_JSON)
        .and()
        .body(updatedStockPayload.toString()).
        when().patch(stocksApi() + "/" + id).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(id)).
        and().body("name", is(name)).
        and().body("currentPrice", is(updatedStockPayload.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(updateInstant))));
  }

  private static Stream<Arguments> invalidPayloadData() {
    return invalidStocksPayloadValuesData();
  }

  @ParameterizedTest
  @MethodSource("invalidPayloadData")
  void testCreateStock_withInvalidJson_shouldFail(String payload, Matcher<String> matcher) {
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
