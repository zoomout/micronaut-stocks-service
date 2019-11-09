package com.bogdan.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {HttpClientResponseException.class, ExceptionHandler.class})
public class HttpClientResponseExceptionHandler implements ExceptionHandler<HttpClientResponseException, HttpResponse> {
  @Override
  public HttpResponse handle(final HttpRequest request, final HttpClientResponseException exception) {
    return HttpResponse.badRequest("TODO"); //TODO
  }
}
