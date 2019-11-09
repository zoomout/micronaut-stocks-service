package com.bogdan.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;

@Introspected
public class StockDto {

  private Integer id;
  @NotEmpty
  private String name;
  @PositiveOrZero
  @NotNull
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
