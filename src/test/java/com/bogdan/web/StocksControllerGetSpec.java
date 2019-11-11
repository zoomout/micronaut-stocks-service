package com.bogdan.web;

import com.bogdan.time.TimeMachine;
import com.bogdan.time.TimeMachineImpl;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bogdan.testdata.Converters.asFloat;
import static com.bogdan.testdata.StocksApiClient.FIST_STOCK_ID;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;

@MicronautTest
class StocksControllerGetSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  @Test
  void testGetStock() {
    JSONObject createdStock = stocksApiClient.createStock();
    when().get(stocksApi() + "/" + FIST_STOCK_ID).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(FIST_STOCK_ID)).
        and().body("name", is(createdStock.get("name"))).
        and().body("currentPrice", is(createdStock.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(timeMachineMock.getTime())))
    );
  }

  @Test
  void testGetMultipleStocks() {
    List<JSONObject> createdStocksList = stocksApiClient.createMultipleStocks(2);
    for (int i = 0; i < createdStocksList.size(); i++) {
      JSONObject createdStock = createdStocksList.get(i);
      when().get(stocksApi() + "/" + i).
          then().assertThat().statusCode(is(OK.getCode())).
          and().body("id", is(i)).
          and().body("name", is(createdStock.get("name"))).
          and().body("currentPrice", is(createdStock.getFloat("currentPrice"))).
          and().body("lastUpdate", is((asFloat(timeMachineMock.getTime())))
      );
    }
  }

  @Test
  void testGetNonExisting_returnsBadRequest() {
    int nonExistingStockId = 1234;
    when().get(stocksApi() + "/" + nonExistingStockId).
        then().assertThat().statusCode(is(BAD_REQUEST.getCode()))
        .and().body(containsString("Entity 'stock' with id '" + nonExistingStockId + "' not found"));
  }

}