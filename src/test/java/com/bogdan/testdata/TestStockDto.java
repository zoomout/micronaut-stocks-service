package com.bogdan.testdata;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestStockDto {

  private Integer id;
  private String name;
  private Double currentPrice;
  private Instant timestamp;

  public Integer getId() {
    return id;
  }

  public TestStockDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public TestStockDto setName(final String name) {
    this.name = name;
    return this;
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }

  public TestStockDto setCurrentPrice(final Double currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public TestStockDto setTimestamp(final Instant timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  @JsonIgnore
  public static TestStockDto generate() {
    return new TestStockDto()
        .setName("name_" + randomAlphabetic(5))
        .setCurrentPrice(1.2)
        .setTimestamp(Instant.ofEpochSecond(0));
  }
}
