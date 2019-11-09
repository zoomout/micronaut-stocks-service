package com.bogdan.service;

import com.bogdan.domain.Stock;
import com.bogdan.dto.StockDto;
import com.bogdan.repository.StocksRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class StocksService {

  @Inject
  private StocksRepository stocksRepository;

  public List<StockDto> getStocks(long offset, long size) {
    return stocksRepository.getPage(offset, size).stream()
        .map(toStockDto())
        .collect(Collectors.toList());
  }


  public StockDto getStock(int id) {
    return toStockDto().apply(stocksRepository.retrieve(id));
  }

  public StockDto createStock(StockDto stockDto) {
    return toStockDto().apply(stocksRepository.create(toStockEntity().apply(stockDto)));
  }

  public StockDto updateStock(int id, StockDto stockDto) {
    return toStockDto().apply(stocksRepository.update(id, toStockEntity().apply(stockDto)));
  }


  private Function<StockDto, Stock> toStockEntity() {
    return stockDto -> new Stock(
        stockDto.getId(),
        stockDto.getName(),
        stockDto.getCurrentPrice(),
        stockDto.getLastUpdate()
    );
  }

  private Function<Stock, StockDto> toStockDto() {
    return stock -> new StockDto(
        stock.getId(),
        stock.getName(),
        stock.getCurrentPrice(),
        stock.getLastUpdate()
    );
  }

}
