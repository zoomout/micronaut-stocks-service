package com.bogdan.repository;

import com.bogdan.domain.StockEntity;

import java.util.List;

public interface StocksRepository {

  int getSize();

  List<StockEntity> getPage(long offset, int size);

  StockEntity create(StockEntity stockEntity);

  StockEntity retrieve(int id);

  StockEntity update(int id, StockEntity stockEntity);

  StockEntity delete(int id);

}
