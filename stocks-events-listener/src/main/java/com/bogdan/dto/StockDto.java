package com.bogdan.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

public class StockDto {

  private Integer id;
  private String name;
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final StockDto that = (StockDto) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(currentPrice, that.currentPrice) &&
        Objects.equals(lastUpdate, that.lastUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, currentPrice, lastUpdate);
  }

  @Override
  public String toString() {
    return "StockDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", currentPrice=" + currentPrice +
        ", lastUpdate=" + lastUpdate +
        '}';
  }
}
