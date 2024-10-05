package com.krecktenwald.runnersutil.service;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
  void handleLogin(HttpServletResponse response);

  void initializeAuth(String code, HttpServletResponse response);
}
