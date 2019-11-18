package com.bogdan.events;

import com.bogdan.dto.StockDto;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Instant;

@MicronautTest
class StocksEventListenerTest {

  @Inject
  private StocksEventListener stocksEventListener;

  @Test
  void testListenerReceiveCall() {
    stocksEventListener.receive("0", new StockDto(0, "", 1.0, Instant.ofEpochSecond(0)));
  }
}
