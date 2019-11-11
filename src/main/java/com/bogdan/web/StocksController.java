package com.bogdan.web;

import com.bogdan.dto.CreateStockDto;
import com.bogdan.dto.ResponseStockDto;
import com.bogdan.dto.UpdateStockDto;
import com.bogdan.service.StocksService;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;

@Validated
@Controller("/api/stocks")
public class StocksController {

  @Inject
  private StocksService stocksService;

  @Get
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse getPage(@Valid Pageable pageable) {
    return HttpResponse.ok(stocksService.getStocks(pageable.getOffset(), pageable.getSize()));
  }

  @Get(value = "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse get(@PositiveOrZero @PathVariable int id) {
    return HttpResponse.ok(stocksService.getStock(id));
  }

  @Post
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse post(@Valid @Body CreateStockDto stockDto) {
    ResponseStockDto stock = stocksService.createStock(stockDto);
    URI uri = URI.create("/api/stocks/" + stock.getId());
    return HttpResponse.created(stock, uri);
  }

  @Patch(value = "/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse patch(@PositiveOrZero Integer id, @Valid @Body UpdateStockDto stockDto) {
    return HttpResponse.ok(stocksService.patchUpdateStock(id, stockDto));
  }
}