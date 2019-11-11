package com.bogdan.repository;

import com.bogdan.domain.StockEntity;

import java.util.List;

public interface StocksRepository {

  int getSize();

  List<StockEntity> getPage(long offset, int size);

  StockEntity findById(int id);

  StockEntity save(StockEntity stockEntity);

  StockEntity deleteById(int id);

}
