package com.krecktenwald.runnersutil.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  @GetMapping("/user-role/is-anonymous")
  public ResponseEntity<Object> isAnonymous() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/user-role/is-admin")
  public ResponseEntity<Object> isAdmin() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/user-role/is-user")
  public ResponseEntity<Object> isUser() {
    return ResponseEntity.ok().build();
  }
}
