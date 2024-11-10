package com.krecktenwald.runnersutil.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
  void handleLogin(HttpServletResponse response);

  void initializeAuth(String code, HttpServletResponse response);

  void refreshToken(HttpServletRequest request, HttpServletResponse response);

  void logout(HttpServletRequest request, HttpServletResponse response);
}
