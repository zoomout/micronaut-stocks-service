package com.bogdan.repository;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TimeMachineTest {

  @Test
  void testTimeMachineReturnsCurrentTime() {
    TimeMachineImpl timeMachine = new TimeMachineImpl();
    Instant time = timeMachine.getTime();
    assertThat(time, notNullValue());
    assertThat(time.isAfter(Instant.now().minusSeconds(10)), is(true));
    assertThat(time.isBefore(Instant.now().plusSeconds(10)), is(true));
  }

  @Test
  void testTimeMachineReturnsTimeTruncatedToMillis() {
    TimeMachineImpl timeMachine = new TimeMachineImpl();
    Instant time = timeMachine.getTime();
    Instant truncated = time.truncatedTo(ChronoUnit.MILLIS);
    assertThat(time, is(truncated));
  }
}
