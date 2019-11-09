package com.bogdan.repository;

import javax.inject.Singleton;
import java.time.Instant;

@Singleton
public class TimeMachineImpl implements TimeMachine {

  @Override
  public Instant getTime() {
    return Instant.now();
  }
}
