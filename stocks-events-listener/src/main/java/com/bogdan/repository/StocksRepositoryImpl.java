package com.bogdan.repository;

import com.bogdan.domain.StockEntity;
import com.bogdan.exception.EntityNotFoundException;
import com.bogdan.time.TimeMachine;

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
  public StockEntity findById(int id) {
    return stocks.get(validated(id));
  }

  @Override
  public synchronized StockEntity save(final StockEntity stockEntity) {
    if (stockEntity.getId() == null) {
      return create(stockEntity);
    } else {
      return update(stockEntity);
    }
  }

  private StockEntity create(final StockEntity stockEntity) {
    StockEntity newStockEntity = new StockEntity(
        stocks.size(),
        stockEntity.getName(),
        stockEntity.getCurrentPrice(),
        timeMachine.getTime()
    );
    stocks.add(newStockEntity);
    return newStockEntity;
  }

  private StockEntity update(final StockEntity stockEntity) {
    StockEntity updated = new StockEntity(
        validated(stockEntity.getId()),
        stockEntity.getName(),
        stockEntity.getCurrentPrice(),
        timeMachine.getTime()
    );
    stocks.set(stockEntity.getId(), updated);
    return updated;
  }

  @Override
  public StockEntity deleteById(int id) {
    return stocks.remove(validated(id));
  }

  private int validated(final int id) {
    if (id >= stocks.size()) {
      throw new EntityNotFoundException("stock", id);
    }
    return id;
  }
}
