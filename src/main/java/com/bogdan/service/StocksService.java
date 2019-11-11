package com.bogdan.service;

import com.bogdan.domain.StockEntity;
import com.bogdan.dto.PatchStockDto;
import com.bogdan.dto.StockDto;
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

  public int getTotal(){
    return stocksRepository.getSize();
  }

  public List<StockDto> getStocks(long offset, int size) {
    return stocksRepository.getPage(offset, size).stream()
        .map(toStockDto())
        .collect(Collectors.toList());
  }

  public StockDto getStock(int id) {
    return toStockDto().apply(stocksRepository.findById(id));
  }

  public StockDto createStock(StockDto stockDto) {
    return toStockDto().apply(stocksRepository.save(toStockEntityCreate().apply(stockDto)));
  }

  public StockDto overrideUpdateStock(int id, StockDto stockDto) {
    return toStockDto().apply(stocksRepository.save(toStockEntityUpdate().apply(stockDto, id)));
  }

  public StockDto patchUpdateStock(int id, PatchStockDto stockDto) {
    StockEntity retrieved = stocksRepository.findById(id);
    return toStockDto().apply(stocksRepository.save(toStockEntityPatch().apply(stockDto, retrieved)));
  }

  private Function<StockDto, StockEntity> toStockEntityCreate() {
    return stockDto -> new StockEntity(
        null,
        stockDto.getName(),
        stockDto.getCurrentPrice(),
        null
    );
  }

  private BiFunction<StockDto, Integer, StockEntity> toStockEntityUpdate() {
    return (stockDto, id) -> new StockEntity(
        id,
        stockDto.getName(),
        stockDto.getCurrentPrice(),
        null
    );
  }

  private BiFunction<PatchStockDto, StockEntity, StockEntity> toStockEntityPatch() {
    return (stockDto, existingEntity) -> new StockEntity(
        existingEntity.getId(),
        stockDto.getName() != null ? stockDto.getName() : existingEntity.getName(),
        stockDto.getCurrentPrice() != null ? stockDto.getCurrentPrice() : existingEntity.getCurrentPrice(),
        null
    );
  }

  private Function<StockEntity, StockDto> toStockDto() {
    return stockEntity -> new StockDto(
        stockEntity.getId(),
        stockEntity.getName(),
        stockEntity.getCurrentPrice(),
        stockEntity.getLastUpdate()
    );
  }

}
