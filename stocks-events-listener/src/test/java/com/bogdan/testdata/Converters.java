package com.bogdan.testdata;

import java.time.Instant;

public class Converters {

  public static float asFloat(Instant instant) {
    if (instant == null) {
      throw new IllegalArgumentException("null argument is not allowed");
    }
    return (float) instant.getEpochSecond() + instant.getNano() / 1_000_000_000f;
  }

  public static float asFloat(double d) {
    return Float.parseFloat(String.valueOf(d));
  }
}
