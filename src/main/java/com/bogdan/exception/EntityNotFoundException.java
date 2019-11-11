package com.bogdan.exception;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String name, int id) {
    super("Entity '" + name + "' with id '" + id + "' not found");
  }

}
