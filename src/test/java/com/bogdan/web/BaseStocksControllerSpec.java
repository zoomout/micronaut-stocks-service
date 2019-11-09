package com.bogdan.web;

import com.bogdan.repository.TimeMachine;
import com.bogdan.testdata.StocksRepositoryTestImpl;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.Instant;

import static org.mockito.Mockito.mock;

public class BaseStocksControllerSpec {

  @Inject
  private StocksRepositoryTestImpl stocksRepository;
  @Inject
  private EmbeddedServer embeddedServer;

  TimeMachine timeMachineMock = mock(TimeMachine.class);
  private static final String STOCKS_BASE_PATH = "/api/stocks";

  String stocksApi() {
    return embeddedServer.getURI().toString() + STOCKS_BASE_PATH;
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


}
