package com.bogdan.web;

import com.bogdan.testdata.StocksRepositoryTestImpl;
import com.bogdan.testdata.TestStockDto;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class StocksControllerSpec {

  @Inject
  private StocksRepositoryTestImpl stocksRepository;

  @Inject
  @Client("/api/stocks")
  HttpClient client;

  @BeforeEach
  void restRepository() {
    stocksRepository.reset();
  }

  @Test
  void testCreateStock() {
    TestStockDto stock = TestStockDto.generate();
    HttpResponse<Object> response = client.toBlocking().exchange(HttpRequest.POST("", "stock"));
    assertEquals(HttpStatus.OK, response.status());
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
  void testGetAllStocks_withPagination(String pagination) {
    String response = client.toBlocking().retrieve(HttpRequest.GET(pagination));
    assertEquals("[]", response);
  }

}