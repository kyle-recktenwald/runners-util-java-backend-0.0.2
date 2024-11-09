package com.krecktenwald.runnersutil.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
  void handleLogin(HttpServletResponse response);

  void initializeAuth(String code, HttpServletResponse response);

  void refreshToken(HttpServletRequest request, HttpServletResponse response);

  ResponseEntity<?> isAuthenticated(HttpServletRequest request);
}
