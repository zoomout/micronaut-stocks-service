package com.bogdan.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class Beans {

  @Singleton
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(FAIL_ON_READING_DUP_TREE_KEY, true);
  }
}
