package com.bogdan.controller;

import com.bogdan.client.StocksServiceClient;
import com.bogdan.dto.StocksDto;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
class StocksListViewController {

  @Inject
  private StocksServiceClient stocksServiceClient;

  @View("stocksList")
  @Get()
  public HttpResponse listStocks(@Valid Pageable pageable) {
    StocksDto stocks = stocksServiceClient.getStocks(pageable);
    return HttpResponse.ok(CollectionUtils.mapOf(
        "total", stocks.getTotal(),
        "stocks", stocks.getStocks()
    ));
  }

}
