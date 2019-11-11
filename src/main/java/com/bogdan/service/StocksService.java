package com.bogdan.service;

import com.bogdan.domain.StockEntity;
import com.bogdan.dto.CreateStockDto;
import com.bogdan.dto.ResponseStockDto;
import com.bogdan.dto.UpdateStockDto;
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
    return toStockDto().apply(stocksRepository.save(toEntityForCreate(stockDto)));
  }

  public ResponseStockDto updateStock(int id, UpdateStockDto stockDto) {
    StockEntity retrievedEntity = stocksRepository.findById(id);
    StockEntity entityForUpdate = toEntityForUpdate(stockDto, retrievedEntity);
    return toStockDto().apply(stocksRepository.save(entityForUpdate));
  }

  private StockEntity toEntityForCreate(final CreateStockDto stockDto) {
    return new StockEntity(
        null,
        stockDto.getName(),
        stockDto.getCurrentPrice(),
        null
    );
  }

  private StockEntity toEntityForUpdate(final UpdateStockDto stockDto, final StockEntity retrievedEntity) {
    return new StockEntity(
        retrievedEntity.getId(),
        retrievedEntity.getName(),
        stockDto.getCurrentPrice() != null ? stockDto.getCurrentPrice() : retrievedEntity.getCurrentPrice(),
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
