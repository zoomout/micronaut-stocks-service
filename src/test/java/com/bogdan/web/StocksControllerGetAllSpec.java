package com.bogdan.web;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

@MicronautTest
class StocksControllerGetAllSpec extends BaseStocksControllerSpec {

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
  void testGetAllStocks_withPagination(String pagination) {
    when().get(stocksApi() + pagination).then().body(is("[]"));
  }


}