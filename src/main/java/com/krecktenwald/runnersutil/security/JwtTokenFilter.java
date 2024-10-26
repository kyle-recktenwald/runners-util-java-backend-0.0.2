package com.krecktenwald.runnersutil.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtDecoder jwtDecoder;

  public JwtTokenFilter(JwtDecoder jwtDecoder) {
    this.jwtDecoder = jwtDecoder;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    setAuthenticationWithJwtInRequestCookie(request);

    try {
      filterChain.doFilter(request, response);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }
  }

  private void setAuthenticationWithJwtInRequestCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
      Arrays.stream(cookies)
          .filter(cookie -> "access_token".equals(cookie.getName()))
          .findFirst()
          .ifPresent(
              cookie -> {
                String jwtToken = cookie.getValue();

                try {
                  // Decode the raw token string into a Jwt object
                  Jwt decodedJwt = jwtDecoder.decode(jwtToken);

                  // Extract roles from the JWT claims (e.g., "roles" claim or Keycloak's
                  // "realm_access")
                  Collection<SimpleGrantedAuthority> authorities = extractAuthorities(decodedJwt);

                  // Create a JwtAuthenticationToken with roles
                  JwtAuthenticationToken authentication =
                      new JwtAuthenticationToken(decodedJwt, authorities);

                  SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (JwtException e) {
                  // Handle decoding or validation failure
                  SecurityContextHolder.clearContext();
                }
              });
    }
  }

  // Extracts roles from JWT and converts them to GrantedAuthority objects
  private Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
    // Assuming roles are in a claim called "roles" or inside "realm_access" (for Keycloak)
    Map<String, Object> realmAccess = jwt.getClaim("realm_access");
    if (realmAccess != null && realmAccess.containsKey("roles")) {
      List<String> roles = (List<String>) realmAccess.get("roles");
      return roles.stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .collect(Collectors.toList());
    }

    // If roles are in a different claim, adjust this logic
    return List.of(); // return empty if no roles found
  }
}
