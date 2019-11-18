package com.bogdan.web;

import com.bogdan.events.StocksEventClient;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
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

  @MockBean(StocksEventClient.class)
  public StocksEventClient stocksEventClient() {
    return stocksEventClient;
  }

  @Test
  void testGetAllStocks_withPagination_andExpectNonEmptyResponse() {
    int amountOfStocks = 5;
    List<JSONObject> createdStocks = stocksApiClient.createMultipleStocks(amountOfStocks);
    int pageSize = 2;

    ValidatableResponse response = when().get(stocksApi() + "?page=1&size=" + pageSize).then();

    assertThat(response.extract().jsonPath().getInt("total"), is(amountOfStocks));
    assertThat(sizeOfStocksArray(response), is(pageSize));
    List<Map> retrievedStocks = extractRetrievedStocksFrom(response);
    assertStockIdsAreEqual(retrievedStocks.get(0), createdStocks.get(2));
    assertStockIdsAreEqual(retrievedStocks.get(1), createdStocks.get(3));
  }

  private int sizeOfStocksArray(final ValidatableResponse response) {
    return response.extract().jsonPath().getList("stocks").size();
  }

  private List<Map> extractRetrievedStocksFrom(final ValidatableResponse response) {
    return response.extract().jsonPath().getList("stocks", Map.class);
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
  void testGetAllStocks_withPagination_andExpectEmptyResponse(String pagination) {
    when().get(stocksApi() + pagination).then().body(is("{\"total\":0}"));
  }

}
