package com.bogdan.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Introspected
public class PatchStockDto {

  @Pattern(regexp = "^(?!\\s*$).+", message = "name: must not be empty")
  private String name;
  @PositiveOrZero
  private Double currentPrice;

  public PatchStockDto(String name, Double currentPrice) {
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
