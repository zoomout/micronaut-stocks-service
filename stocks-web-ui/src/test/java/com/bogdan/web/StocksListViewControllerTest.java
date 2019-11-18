package com.bogdan.web;

import com.bogdan.client.StocksServiceClient;
import com.bogdan.dto.StocksDto;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@MicronautTest
class StocksListViewControllerTest extends BaseStocksControllerSpec {

  @MockBean(StocksServiceClient.class)
  public StocksServiceClient stocksServiceClient() {
    return stocksServiceClient;
  }

  @Test
  void testStocksListView_shouldReturnSuccessfulResponse_withPayload() {
    int amountOfStocks = 2;
    mockStocksServiceClient(new StocksDto(amountOfStocks, new ArrayList<>()));
    ValidatableResponse response = when().get(baseUri()).then();
    response.statusCode(is(200));
    response.assertThat().body("html.body.section.h1.span", equalTo(String.valueOf(amountOfStocks)));
  }

}
