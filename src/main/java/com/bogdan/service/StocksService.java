package com.bogdan.service;

import com.bogdan.domain.StockEntity;
import com.bogdan.dto.CreateStockDto;
import com.bogdan.dto.StockDto;
import com.bogdan.dto.UpdateStockDto;
import com.bogdan.events.StocksEventClient;
import com.bogdan.repository.StocksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class StocksService {

  private static final Logger LOG = LoggerFactory.getLogger(StocksService.class);

  @Inject
  private StocksRepository stocksRepository;

  @Inject
  private StocksEventClient stocksEventClient;

  public int getTotal() {
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

  public StockDto createStock(CreateStockDto stockDto) {
    StockDto createdStockDto = toStockDto().apply(stocksRepository.save(toEntityForCreate(stockDto)));
    LOG.info("Sending: " + createdStockDto);
    stocksEventClient.sendStock(String.valueOf(createdStockDto.getId()), createdStockDto);
    LOG.info("Sent: " + createdStockDto);
    return createdStockDto;
  }

  public StockDto updateStock(int id, UpdateStockDto stockDto) {
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

  private Function<StockEntity, StockDto> toStockDto() {
    return stockEntity -> new StockDto(
        stockEntity.getId(),
        stockEntity.getName(),
        stockEntity.getCurrentPrice(),
        stockEntity.getLastUpdate()
    );
  }

}
