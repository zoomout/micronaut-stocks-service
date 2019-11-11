package com.bogdan.web;

import com.bogdan.time.TimeMachine;
import com.bogdan.time.TimeMachineImpl;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.bogdan.testdata.Converters.asFloat;
import static com.bogdan.testdata.SharedTestDataParameters.invalidStocksPayloadValuesData;
import static com.bogdan.testdata.StocksApiClient.FIST_STOCK_ID;
import static io.micronaut.http.HttpHeaders.LOCATION;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;

@MicronautTest
class StocksControllerPostSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  private static Stream<Arguments> validPayloadData() {
    return Stream.of(
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_1")
                .accumulate("currentPrice", 1.23)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("id", 1234) // should ignore a number
                .accumulate("name", "valid_name_2")
                .accumulate("currentPrice", 1.24)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("id", "abc") // should ignore a string
                .accumulate("name", "valid_name_2")
                .accumulate("currentPrice", 1.24)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_3")
                .accumulate("currentPrice", 1.2)
                .accumulate("lastUpdate", 1.123) // should ignore a number
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_3")
                .accumulate("currentPrice", 1.2)
                .accumulate("lastUpdate", "abc") // should ignore a string
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_4")
                .accumulate("currentPrice", 1.25)
                .accumulate("unknown", "someValue") // should ignore
        )
    );
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

  private static Stream<Arguments> missingFields() {
    return Stream.of(
        Arguments.of(
            new JSONObject().toString(), // missing name and currentPrice
            both(containsString("currentPrice: must not be null"))
                .and(containsString("name: must not be empty"))
        ),
        Arguments.of(
            new JSONObject().accumulate("name", "name_1").toString(), // missing currentPrice
            containsString("currentPrice: must not be null")
        ),
        Arguments.of(
            new JSONObject().accumulate("currentPrice", 1.23).toString(), // missing name
            containsString("name: must not be empty")
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "") // empty name
                .accumulate("currentPrice", 1.1).toString(),
            containsString("name: must not be empty")
        )
    );
  }

  private static Stream<Arguments> invalidPayloadData() {
    return Stream.concat(invalidStocksPayloadValuesData(), missingFields());
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