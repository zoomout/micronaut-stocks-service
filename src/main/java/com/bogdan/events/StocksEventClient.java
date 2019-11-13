package com.bogdan.events;

import com.bogdan.dto.StockDto;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;

@KafkaClient
public interface StocksEventClient {

  @Topic("micronaut-stocks")
  void sendStock(@KafkaKey String id, @Body StockDto stockDto);
}
