package com.krecktenwald.runnersutil.controller;

import com.krecktenwald.runnersutil.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

  private static final Logger logger = LogManager.getLogger(AuthController.class);

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/public/oauth/login")
  public void login(HttpServletResponse response) {
    authService.handleLogin(response);
  }

  @GetMapping(value = "/public/oauth/callback")
  @PermitAll
  public RedirectView oauthCallback(
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "state", required = false) String state,
      HttpServletRequest request,
      HttpServletResponse response) {

    if (code != null) {
      authService.initializeAuth(code, response);
    }

    return new RedirectView("https://runnersutil.local");
  }

  @PostMapping("/oauth/refresh")
  public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    try {
      authService.refreshToken(request, response);
      return ResponseEntity.ok(Map.of("message", "Token refreshed successfully"));
    } catch (Exception e) {
      logger.error("Failed to refresh token", e);
      return ResponseEntity.status(401).body(Map.of("message", "Unable to refresh token"));
    }
  }

  @GetMapping("/oauth/isAuthenticated")
  public ResponseEntity<?> isAuthenticated(HttpServletRequest request) {
    return authService.isAuthenticated(request);
  }
}
