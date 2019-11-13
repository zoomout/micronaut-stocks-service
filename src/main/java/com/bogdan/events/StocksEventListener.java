package com.bogdan.events;

import com.bogdan.dto.StockDto;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(offsetReset = OffsetReset.EARLIEST, groupId = "stock-listener")
public class StocksEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(StocksEventListener.class);

  @Topic("micronaut-stocks")
  public void receive(@KafkaKey String id, @Body StockDto stockDto) {
    LOG.info("Received with id '" + id + "': " + stockDto);
  }

}
