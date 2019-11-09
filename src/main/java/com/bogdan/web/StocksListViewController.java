package com.bogdan.web;

import com.bogdan.dto.StockDto;
import com.bogdan.service.StocksService;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import javax.inject.Inject;
import java.util.List;

@Controller
class StocksListViewController {

  @Inject
  private StocksService stocksService;

  @View("stocksList")
  @Get()
  public HttpResponse listStocks() {
    List<StockDto> stocks = stocksService.getStocks(0, 20); //TODO implement pagination
    return HttpResponse.ok(CollectionUtils.mapOf(
        "amount", 5,
        "stocks", stocks
    ));
  }

}
