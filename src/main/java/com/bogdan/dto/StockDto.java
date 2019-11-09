package com.bogdan.dto;

import java.time.Instant;

public class StockDto {

  private Integer id;
  private String name;
  private Double currentPrice;
  private Instant lastUpdate;

  public StockDto(
      Integer id,
      String name,
      Double currentPrice,
      Instant lastUpdate
  ) {
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
