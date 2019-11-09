package com.bogdan.web;

import com.bogdan.repository.TimeMachine;
import com.bogdan.repository.TimeMachineImpl;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.bogdan.testdata.Converters.asFloat;
import static io.micronaut.http.HttpStatus.*;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;

@MicronautTest
class StocksControllerGetSpec extends BaseStocksControllerSpec {

  @MockBean(TimeMachineImpl.class)
  public TimeMachine timeMachine() {
    return timeMachineMock;
  }

  private static final int FIST_STOCK_ID = 0;

  @Test
  void testGetCreatedStock() {
    JSONObject createdStock = createStock();
    when().get(stocksApi() + "/" + FIST_STOCK_ID).
        then().assertThat().statusCode(is(OK.getCode())).
        and().body("id", is(FIST_STOCK_ID)).
        and().body("name", is(createdStock.get("name"))).
        and().body("currentPrice", is(createdStock.getFloat("currentPrice"))).
        and().body("lastUpdate", is((asFloat(timeMachineMock.getTime())))
    );
  }

  @Test
  void testGetMultipleCreatedStocks() {
    List<JSONObject> createdStocksList = createMultipleStocks(2);
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

  private List<JSONObject> createMultipleStocks(int amount) {
    ArrayList<JSONObject> stocks = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      JSONObject stock = createStock(i);
      stocks.add(stock);
    }
    return stocks;
  }

  private JSONObject createStock() {
    return createStock(FIST_STOCK_ID);
  }

  private JSONObject createStock(int id) {
    JSONObject stockPayload = new JSONObject()
        .accumulate("name", "name_" + id)
        .accumulate("currentPrice", 1.2);
    given().contentType(APPLICATION_JSON)
        .and()
        .body(stockPayload.toString()).
        when().post(stocksApi()).
        then().assertThat().statusCode(is(CREATED.getCode()));
    return stockPayload;
  }

}