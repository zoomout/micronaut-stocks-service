package com.bogdan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class StockCreateDto extends StockDto {

  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private String name;
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private Double currentPrice;

  public StockCreateDto(
      final Integer id,
      final String name,
      final Double currentPrice,
      final Instant lastUpdate
  ) {
    super(id, name, currentPrice, lastUpdate);
  }
}
