package com.bogdan.testdata;

import org.json.JSONObject;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class SharedTestDataParameters {

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

}
