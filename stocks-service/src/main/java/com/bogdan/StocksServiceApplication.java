package com.bogdan;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Micronaut stock service",
        version = "0.1",
        description = "Demo project using Micronaut framework "
    )
)
public class StocksServiceApplication {
  public static void main(String[] args) {
    Micronaut.run(StocksServiceApplication.class);
  }
}