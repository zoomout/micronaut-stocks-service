package com.bogdan.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;

@Introspected
public class StockDto {

  private Integer id;
  @NotBlank
  private String name;
  @PositiveOrZero
  private Double currentPrice;
  private Instant lastUpdate;

  @JsonCreator
  public StockDto(
      @JsonProperty("id") Integer id,
      @JsonProperty("name") String name,
      @JsonProperty("currentPrice") Double currentPrice,
      @JsonProperty("lastUpdate") Instant lastUpdate
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
