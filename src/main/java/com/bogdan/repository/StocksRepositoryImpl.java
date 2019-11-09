package com.bogdan.repository;

import com.bogdan.domain.StockEntity;
import com.bogdan.error.EntityNotFoundException;

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
    return stocks.get(validated(id));
  }

  @Override
  public synchronized StockEntity create(StockEntity stockEntity) {
    StockEntity newStockEntity = new StockEntity(stocks.size(), stockEntity.getName(), stockEntity.getCurrentPrice(), timeMachine.getTime());
    stocks.add(newStockEntity);
    return newStockEntity;
  }

  @Override
  public synchronized StockEntity update(int id, StockEntity stockEntity) {
    StockEntity current = stocks.get(validated(id));
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
    return stocks.remove(validated(id));
  }

  private int validated(final int id) {
    if (id >= stocks.size()) {
      throw new EntityNotFoundException("stock", id);
    }
    return id;
  }
}
