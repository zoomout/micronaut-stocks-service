package com.bogdan.client;

import com.bogdan.dto.StocksDto;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.validation.Validated;

import javax.validation.Valid;

@Client("${stocks-service.url}")
@Validated
public interface StocksServiceClient {

  @Get("/api/stocks")
  StocksDto getStocks(@Valid Pageable pageable);

}