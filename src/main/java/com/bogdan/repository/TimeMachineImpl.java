package com.bogdan.repository;

import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Singleton
public class TimeMachineImpl implements TimeMachine {

  @Override
  public Instant getTime() {
    return Instant.now().truncatedTo(ChronoUnit.MILLIS);
  }
}
