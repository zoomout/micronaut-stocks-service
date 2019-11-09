package com.bogdan.repository;

import com.bogdan.domain.StockEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Singleton
public class StocksRepositoryImpl implements StocksRepository {

  private List<StockEntity> stocks = new CopyOnWriteArrayList<>();

  @Inject
  private TimeMachine timeMachine;

  @Override
  public int getSize() {
    return stocks.size();
  }

  @Override
  public List<StockEntity> getPage(long offset, int size) {
    return stocks.stream().skip(offset).limit(size).collect(Collectors.toList());
  }

  @Override
  public StockEntity retrieve(int id) {
    if (id >= stocks.size()) {
      return null; // TODO throw exception
    }
    return stocks.get(id);
  }

  @Override
  public synchronized StockEntity create(StockEntity stockEntity) {
    StockEntity newStockEntity = new StockEntity(stocks.size(), stockEntity.getName(), stockEntity.getCurrentPrice(), timeMachine.getTime());
    stocks.add(newStockEntity);
    return newStockEntity;
  }

  @Override
  public synchronized StockEntity update(int id, StockEntity stockEntity) {
    if (id >= stocks.size()) {
      return null; // TODO throw exception
    }
    StockEntity current = stocks.get(id);
    StockEntity updated = new StockEntity(
        current.getId(),
        stockEntity.getName() != null ? stockEntity.getName() : current.getName(),
        stockEntity.getCurrentPrice() != null ? stockEntity.getCurrentPrice() : current.getCurrentPrice(),
        timeMachine.getTime()
    );
    stocks.set(id, updated);
    return updated;
  }

  @Override
  public StockEntity delete(int id) {
    if (id >= stocks.size()) {
      return null; // TODO throw exception
    }
    return stocks.remove(id);
  }
}
