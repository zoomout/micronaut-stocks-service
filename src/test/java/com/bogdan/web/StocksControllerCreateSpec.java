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
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

@MicronautTest
class StocksControllerCreateSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  private static final int FIST_STOCK_ID = 0;

  private static Stream<Arguments> validPayloadData() {
    return Stream.of(
        Arguments.of(
            new JSONObject()
                .accumulate("name", "name_1")
                .accumulate("currentPrice", 1.2)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("id", 1) // should ignore
                .accumulate("name", "name_1")
                .accumulate("currentPrice", 1.2)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "name_1")
                .accumulate("currentPrice", 1.2)
                .accumulate("lastUpdate", 1.123) // should ignore
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "name_1")
                .accumulate("currentPrice", 1.2)
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
        and().body("lastUpdate", is((asFloat(timeMachineMock.getTime())))
    );
  }

  private static Stream<Arguments> invalidPayloadData() {
    return Stream.of(
        Arguments.of(
            "",
            matchesPattern("Required Body (.*) not specified")
        ),
        Arguments.of(
            "invalid json",
            startsWith("Invalid JSON")
        ),
        Arguments.of(
            new JSONObject().toString(), // missing name and currentPrice
            matchesPattern("(.*) must not be empty")
        ),
        Arguments.of(
            new JSONObject().accumulate("name", "name_1").toString(), // missing currentPrice
            matchesPattern("oh no currentPrice is missing!")
        ),
        Arguments.of(
            new JSONObject().accumulate("currentPrice", 1.23).toString(), // missing name
            matchesPattern("oh no name is missing!")
        )
    );
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
        .and().body("message", matcher);
  }

}