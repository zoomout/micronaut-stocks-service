package com.bogdan.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.PositiveOrZero;

@Introspected
public class UpdateStockDto {

  @PositiveOrZero
  private Double currentPrice;

  public UpdateStockDto(Double currentPrice) {
    this.currentPrice = currentPrice;
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }
}
