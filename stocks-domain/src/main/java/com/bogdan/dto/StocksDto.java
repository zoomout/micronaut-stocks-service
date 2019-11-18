package com.bogdan.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class StocksDto {
  private Integer total;
  private List<StockDto> stocks;

  @JsonCreator
  public StocksDto(
      @JsonProperty("total") final Integer total,
      @JsonProperty("stocks") final List<StockDto> stocks
  ) {
    this.total = total;
    this.stocks = stocks;
  }

  public Integer getTotal() {
    return total;
  }

  public List<StockDto> getStocks() {
    return stocks;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final StocksDto stocksDto = (StocksDto) o;
    return Objects.equals(total, stocksDto.total) &&
        Objects.equals(stocks, stocksDto.stocks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, stocks);
  }

  @Override
  public String toString() {
    return "StocksDto{" +
        "total=" + total +
        ", stocks=" + stocks +
        '}';
  }
}
