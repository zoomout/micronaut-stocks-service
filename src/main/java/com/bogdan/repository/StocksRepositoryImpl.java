package com.bogdan.repository;

import com.bogdan.domain.Stock;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Singleton
public class StocksRepositoryImpl implements StocksRepository {

  private List<Stock> stocks = new CopyOnWriteArrayList<>();

  @Inject
  private TimeMachine timeMachine;

  @Override
  public int getSize() {
    return stocks.size();
  }

  @Override
  public List<Stock> getPage(long offset, long size) {
    return stocks.stream().skip(offset).limit(size).collect(Collectors.toList());
  }

  @Override
  public Stock retrieve(int id) {
    return stocks.get(id);
  }

  @Override
  public synchronized Stock create(Stock stock) {
    Stock newStock = new Stock(stocks.size(), stock.getName(), stock.getCurrentPrice(), timeMachine.getTime());
    stocks.add(newStock);
    return newStock;
  }

  @Override
  public synchronized Stock update(int id, Stock stock) {
    if (id >= stocks.size()) {
      return null;
    }
    Stock current = stocks.get(id);
    Stock updated = new Stock(
        current.getId(),
        stock.getName() != null ? stock.getName() : current.getName(),
        stock.getCurrentPrice() != null ? stock.getCurrentPrice() : current.getCurrentPrice(),
        timeMachine.getTime()
    );
    stocks.set(id, updated);
    return updated;
  }

  @Override
  public Stock delete(int id) {
    if (id >= stocks.size()) {
      return null;
    }
    return stocks.remove(id);
  }
}
