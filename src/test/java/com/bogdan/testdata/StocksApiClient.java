package com.bogdan.testdata;

import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class StocksApiClient {

  public static final int FIST_STOCK_ID = 0;

  private String apiUrl;

  public StocksApiClient(final String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public List<JSONObject> createMultipleStocks(int amount) {
    ArrayList<JSONObject> stocks = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      JSONObject stock = createStock(i);
      stocks.add(stock);
    }
    return stocks;
  }

  public JSONObject createStock() {
    return createStock(FIST_STOCK_ID);
  }

  private JSONObject createStock(int index) {
    JSONObject stockPayload = new JSONObject()
        .accumulate("name", "name_" + index)
        .accumulate("currentPrice", 1.2);
    ValidatableResponse validatableResponse = given().contentType(APPLICATION_JSON)
        .and()
        .body(stockPayload.toString()).
            when().post(apiUrl).
            then()
        .assertThat().statusCode(is(CREATED.getCode()));
    int id = validatableResponse.extract().body().jsonPath().getInt("id");
    float lastUpdate = validatableResponse.extract().body().jsonPath().getFloat("lastUpdate");
    stockPayload.accumulate("id", id);
    stockPayload.accumulate("lastUpdate", lastUpdate);
    return stockPayload;
  }

}
