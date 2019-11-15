package com.bogdan.web;

import com.bogdan.events.StocksEventClient;
import com.bogdan.testdata.StocksApiClient;
import com.bogdan.testdata.StocksRepositoryTestImpl;
import com.bogdan.time.TimeMachine;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BaseStocksControllerSpec {

  @Inject
  private StocksRepositoryTestImpl stocksRepository;
  @Inject
  private EmbeddedServer embeddedServer;

  StocksApiClient stocksApiClient;

  TimeMachine timeMachineMock = mock(TimeMachine.class);
  StocksEventClient stocksEventClient = mock(StocksEventClient.class);

  private static final String STOCKS_BASE_PATH = "/api/stocks";

  String stocksApi() {
    return embeddedServer.getURI().toString() + STOCKS_BASE_PATH;
  }

  @BeforeEach
  void createStocksApiClient() {
    stocksApiClient = new StocksApiClient(stocksApi());
  }

  @BeforeEach
  void restRepository() {
    stocksRepository.reset();
  }

  @BeforeEach
  void setTime() {
    final Instant mockedTime = Instant.ofEpochSecond(1).plusMillis(1);
    Mockito.when(timeMachineMock.getTime()).thenReturn(mockedTime);
  }

  @BeforeEach
  void mockEventClientAndListener() {
    doNothing().when(stocksEventClient).sendStock(any(), any());
  }

  void changeTime(final Instant updateTime) {
    when(timeMachineMock.getTime()).thenReturn(updateTime);
  }

}
