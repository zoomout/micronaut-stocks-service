package com.bogdan.repository;

import com.bogdan.domain.Stock;

import java.util.List;

public interface StocksRepository {

  int getSize();

  List<Stock> getPage(long offset, long size);

  Stock create(Stock stock);

  Stock retrieve(int id);

  Stock update(int id, Stock stock);

  Stock delete(int id);

}
