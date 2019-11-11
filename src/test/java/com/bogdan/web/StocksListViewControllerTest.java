package com.bogdan.web;

import io.micronaut.test.annotation.MicronautTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@MicronautTest
public class StocksListViewControllerTest extends BaseStocksControllerSpec {

  @Test
  void testStocksListView_shouldReturnSuccessfulResponse_withPayload() {
    int amountOfStocks = 2;
    stocksApiClient.createMultipleStocks(amountOfStocks);

    ValidatableResponse response = when().get("/").then();
    response.statusCode(is(200));
    response.assertThat().body("html.body.section.h1.span", equalTo(String.valueOf(amountOfStocks)));
  }

}
