package com.bogdan.web;

import com.bogdan.client.StocksServiceClient;
import com.bogdan.dto.StocksDto;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseStocksControllerSpec {

  @Inject
  private EmbeddedServer embeddedServer;

  StocksServiceClient stocksServiceClient = mock(StocksServiceClient.class);

  String baseUri() {
    return embeddedServer.getURI().toString();
  }

  void mockStocksServiceClient(StocksDto stocks) {
    when(stocksServiceClient.getStocks(any())).thenReturn(stocks);
  }

}
