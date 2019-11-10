package com.bogdan.testdata;

import org.json.JSONObject;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class TestDataParameters {

  public static Stream<Arguments> invalidStocksPayloadValuesData() {
    return Stream.of(
        Arguments.of(
            "",
            matchesPattern("(.*)Required Body (.*) not specified(.*)")
        ),
        Arguments.of(
            "invalid json",
            containsString("Invalid JSON")
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "") // empty name
                .accumulate("currentPrice", 1.1).toString(),
            containsString("name: must not be empty")
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "name_1")
                .accumulate("currentPrice", -1.1).toString(), // negative currentPrice
            containsString("currentPrice: must be greater than or equal to 0")
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "name_1")
                .accumulate("currentPrice", "stringIsInvalidType").toString(), // invalid currentPrice type
            containsString("not a valid Double value")
        )
    );
  }

  public static Stream<Arguments> missingFields() {
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
        )
    );
  }

  public static Stream<Arguments> validStocksPayloadData() {
    return Stream.of(
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_1")
                .accumulate("currentPrice", 1.23)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("id", 1234) // should ignore
                .accumulate("name", "valid_name_2")
                .accumulate("currentPrice", 1.24)
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_3")
                .accumulate("currentPrice", 1.2)
                .accumulate("lastUpdate", 1.123) // should ignore
        ),
        Arguments.of(
            new JSONObject()
                .accumulate("name", "valid_name_4")
                .accumulate("currentPrice", 1.25)
                .accumulate("unknown", "someValue") // should ignore
        )
    );
  }
}
