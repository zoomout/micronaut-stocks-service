package com.bogdan.domain;

import java.time.Instant;

public class Stock {
  private Integer id;
  private String name;
  private Double currentPrice;
  private Instant lastUpdate;

  public Stock(Integer id, String name, Double currentPrice, Instant lastUpdate) {
    this.id = id;
    this.name = name;
    this.currentPrice = currentPrice;
    this.lastUpdate = lastUpdate;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }

  public Instant getLastUpdate() {
    return lastUpdate;
  }
}
