package com.krecktenwald.runnersutil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No entity found")
public class EntityNotFoundException extends RuntimeException {
  private final String msg;

  public EntityNotFoundException(String msg) {
    super(msg);
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
