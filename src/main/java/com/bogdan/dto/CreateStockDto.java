package com.bogdan.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Introspected
public class CreateStockDto {

  @NotEmpty
  private String name;
  @PositiveOrZero
  @NotNull
  private Double currentPrice;

  public CreateStockDto(
      String name,
      Double currentPrice
  ) {
    this.name = name;
    this.currentPrice = currentPrice;
  }

  public String getName() {
    return name;
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }

}
