package com.bogdan.service;

import com.bogdan.domain.StockEntity;
import com.bogdan.dto.CreateStockDto;
import com.bogdan.dto.ResponseStockDto;
import com.bogdan.dto.UpdateStockDto;
import com.bogdan.repository.StocksRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class StocksService {

  @Inject
  private StocksRepository stocksRepository;

  public int getTotal() {
    return stocksRepository.getSize();
  }

  public List<ResponseStockDto> getStocks(long offset, int size) {
    return stocksRepository.getPage(offset, size).stream()
        .map(toStockDto())
        .collect(Collectors.toList());
  }

  public ResponseStockDto getStock(int id) {
    return toStockDto().apply(stocksRepository.findById(id));
  }

  public ResponseStockDto createStock(CreateStockDto stockDto) {
    return toStockDto().apply(stocksRepository.save(toStockEntityCreate().apply(stockDto)));
  }

  public ResponseStockDto patchUpdateStock(int id, UpdateStockDto stockDto) {
    StockEntity retrieved = stocksRepository.findById(id);
    return toStockDto().apply(stocksRepository.save(toStockEntityPatch().apply(stockDto, retrieved)));
  }

  private Function<CreateStockDto, StockEntity> toStockEntityCreate() {
    return stockDto -> new StockEntity(
        null,
        stockDto.getName(),
        stockDto.getCurrentPrice(),
        null
    );
  }

  private BiFunction<UpdateStockDto, StockEntity, StockEntity> toStockEntityPatch() {
    return (stockDto, existingEntity) -> new StockEntity(
        existingEntity.getId(),
        existingEntity.getName(),
        stockDto.getCurrentPrice() != null ? stockDto.getCurrentPrice() : existingEntity.getCurrentPrice(),
        null
    );
  }

  private Function<StockEntity, ResponseStockDto> toStockDto() {
    return stockEntity -> new ResponseStockDto(
        stockEntity.getId(),
        stockEntity.getName(),
        stockEntity.getCurrentPrice(),
        stockEntity.getLastUpdate()
    );
  }

}
