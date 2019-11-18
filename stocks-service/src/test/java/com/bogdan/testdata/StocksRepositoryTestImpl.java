package com.bogdan.testdata;

import com.bogdan.repository.StocksRepositoryImpl;
import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;

@Replaces(StocksRepositoryImpl.class)
@Singleton
public class StocksRepositoryTestImpl extends StocksRepositoryImpl {

  public void reset() {
    int size = super.getSize();
    for (int i = size - 1; i >= 0; i--) {
      super.deleteById(i);
    }
  }

}
