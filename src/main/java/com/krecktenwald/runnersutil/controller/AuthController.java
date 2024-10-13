package com.krecktenwald.runnersutil.controller;

import com.krecktenwald.runnersutil.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/oauth/login")
  public void login(HttpServletResponse response) {
    authService.handleLogin(response);
  }

  @GetMapping(value = "/oauth/callback")
  @PermitAll
  public RedirectView oauthCallback(
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "state", required = false) String state,
      HttpServletRequest request,
      HttpServletResponse response) {

    if (code != null) {
      authService.initializeAuth(code, response);
    }

    return new RedirectView("http://localhost:3000");
  }
}
