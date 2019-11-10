package com.bogdan.web;

import com.bogdan.repository.TimeMachine;
import com.bogdan.repository.TimeMachineImpl;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.bogdan.testdata.Converters.asFloat;
import static com.bogdan.testdata.StocksApiClient.FIST_STOCK_ID;
import static com.bogdan.testdata.TestDataParameters.invalidStocksPayloadData;
import static com.bogdan.testdata.TestDataParameters.validStocksPayloadData;
import static io.micronaut.http.HttpHeaders.LOCATION;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@MicronautTest
class StocksControllerPostSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  private static Stream<Arguments> validPayloadData() {
    return validStocksPayloadData();
  }

  @ParameterizedTest
  @MethodSource("validPayloadData")
  void testCreateFirstValidStock(JSONObject payload) {
    given().contentType(APPLICATION_JSON)
        .and()
        .body(payload.toString()).
        when().post(stocksApi()).
        then().assertThat().statusCode(is(CREATED.getCode())).
        and().body("id", is(FIST_STOCK_ID)).
        and().body("name", is(payload.get("name"))).
        and().body("currentPrice", is(payload.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(timeMachineMock.getTime())))).
        and().header(LOCATION, "/api/stocks/" + FIST_STOCK_ID);
  }

  private static Stream<Arguments> invalidPayloadData() {
    return invalidStocksPayloadData();
  }

  @ParameterizedTest
  @MethodSource("invalidPayloadData")
  void testCreateStock_invalidJson(String payload, Matcher<String> matcher) {
    given()
        .contentType(APPLICATION_JSON)
        .and().body(payload)
        .when()
        .post(stocksApi())
        .then().statusCode(is(BAD_REQUEST.getCode()))
        .and().body(matcher);
  }

}