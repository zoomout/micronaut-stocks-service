package com.bogdan.web;

import io.micronaut.test.annotation.MicronautTest;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@MicronautTest
class StocksControllerGetAllSpec extends BaseStocksControllerSpec {

  @Test
  void testGetAllStocks_withPagination_whenNonEmptyResponse() {
    List<JSONObject> createdStocks = stocksApiClient.createMultipleStocks(5);
    int pageSize = 2;

    ValidatableResponse response = when().get(stocksApi() + "?page=1&size=" + pageSize).then();

    assertThat(sizeOfArrayIn(response), is(pageSize));
    List<Map> retrievedStocks = extractRetrievedStocksFrom(response);
    assertStockIdsAreEqual(retrievedStocks.get(0), createdStocks.get(2));
    assertStockIdsAreEqual(retrievedStocks.get(1), createdStocks.get(3));
  }

  private int sizeOfArrayIn(final ValidatableResponse response) {
    return response.extract().jsonPath().getList("").size();
  }

  private List<Map> extractRetrievedStocksFrom(final ValidatableResponse response) {
    return response.extract().jsonPath().getList("", Map.class);
  }

  private void assertStockIdsAreEqual(final Map actual, final JSONObject expected) {
    assertThat(actual.get("id"), is(expected.get("id")));
  }

  @ParameterizedTest
  @ValueSource
      (strings = {
          "",
          "?page=0&size=0",
          "?page=0&size=null",
          "?page=null&size=0",
          "?page=1&size=0",
          "?page=0&size=1",
          "?page=0",
          "?size=0"
      })
  void testGetAllStocks_withPagination_whenEmptyResponse(String pagination) {
    when().get(stocksApi() + pagination).then().body(is("[]"));
  }

}