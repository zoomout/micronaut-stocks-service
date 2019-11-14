package com.bogdan.web;

import com.bogdan.dto.StockDto;
import com.bogdan.service.StocksService;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@Controller
class StocksListViewController {

  @Inject
  private StocksService stocksService;

  @View("stocksList")
  @Get()
  public HttpResponse listStocks(@Valid Pageable pageable) {
    int total = stocksService.getTotal();
    List<StockDto> stocks = stocksService.getStocks(pageable.getOffset(), pageable.getSize());
    return HttpResponse.ok(CollectionUtils.mapOf(
        "total", total,
        "stocks", stocks
    ));
  }

}
