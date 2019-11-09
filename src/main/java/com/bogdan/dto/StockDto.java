package com.bogdan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.time.Instant;

@Introspected
public class StockDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer id;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String name;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double currentPrice;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
