package com.krecktenwald.runnersutil.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtDecoder jwtDecoder;
  private final String keycloakRealm;
  private final String clientId;

  public JwtService(
      JwtDecoder jwtDecoder,
      @Value("${keycloak.oauth2.realm}") String keycloakRealm,
      @Value("${jwt.auth.converter.resource-id}") String clientId) {
    this.jwtDecoder = jwtDecoder;
    this.keycloakRealm = keycloakRealm;
    this.clientId = clientId;
  }

  public String getUserIdFromJwt() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();
    return jwt.getSubject();
  }

  public String getJwtFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }

    Optional<Cookie> jwtCookie =
        Arrays.stream(cookies)
            .filter(cookie -> "access_token".equals(cookie.getName()))
            .findFirst();

    return jwtCookie.map(Cookie::getValue).orElse(null);
  }

  public boolean isValidToken(String token) {
    try {
      Jwt jwt = jwtDecoder.decode(token);

      if (!isIssuerValid(jwt)) {
        return false;
      }

      if (!isAudienceValid(jwt)) {
        return false;
      }

      if (isTokenExpired(jwt)) {
        return false;
      }

      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  private boolean isIssuerValid(Jwt jwt) {
    return keycloakRealm.equals(jwt.getIssuer().toString());
  }

  private boolean isAudienceValid(Jwt jwt) {
    return clientId.equals(jwt.getClaimAsString("azp"));
  }

  private boolean isTokenExpired(Jwt jwt) {
    Instant expiration = jwt.getExpiresAt();
    return expiration != null && Instant.now().isAfter(expiration);
  }
}
