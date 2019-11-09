package com.bogdan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class StockUpdateDto extends StockDto {

  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private Double currentPrice;

  public StockUpdateDto(
      final Integer id,
      final String name,
      final Double currentPrice,
      final Instant lastUpdate
  ) {
    super(id, name, currentPrice, lastUpdate);
  }
}
