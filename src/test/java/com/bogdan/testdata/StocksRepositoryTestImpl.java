package com.bogdan.testdata;

import com.bogdan.repository.StocksRepositoryImpl;
import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;

@Replaces(StocksRepositoryImpl.class)
@Singleton
public class StocksRepositoryTestImpl extends StocksRepositoryImpl {

  public void reset() {
    int size = super.getSize();
    for (int i = 0; i < size; i++) {
      super.deleteById(i);
    }
  }

}
