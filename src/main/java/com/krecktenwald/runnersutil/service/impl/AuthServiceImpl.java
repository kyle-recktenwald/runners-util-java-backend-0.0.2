package com.krecktenwald.runnersutil.service.impl;

import com.krecktenwald.runnersutil.domain.util.auth.JwtValues;
import com.krecktenwald.runnersutil.security.JwtService;
import com.krecktenwald.runnersutil.service.AuthService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

  private final WebClient webClient;
  private final JwtService jwtService;
  private final String baseAuthUri;
  private final String baseTokenUri;
  private final String logoutUri;
  private final String clientId;
  private final String clientSecret;
  private final String redirectUri;
  private final String codeChallenge;
  private final String codeVerifier;

  @Autowired
  public AuthServiceImpl(
      WebClient webClient,
      JwtService jwtService,
      @Value("${keycloak.oauth2.auth-uri}") String baseAuthUri,
      @Value("${keycloak.oauth2.token-uri}") String baseTokenUri,
      @Value("${keycloak.oauth2.logout-uri}") String logoutUri,
      @Value("${jwt.auth.converter.resource-id}") String clientId,
      @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
          String clientSecret,
      @Value("${keycloak.oauth2.redirect-uri}") String redirectUri,
      @Value("${keycloak.oauth2.code-challenge}") String codeChallenge,
      @Value("${keycloak.oauth2.code-verifier}") String codeVerifier) {
    this.webClient = webClient;
    this.jwtService = jwtService;
    this.baseAuthUri = baseAuthUri;
    this.baseTokenUri = baseTokenUri;
    this.logoutUri = logoutUri;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
    this.codeChallenge = codeChallenge;
    this.codeVerifier = codeVerifier;
  }

  @Override
  public void handleLogin(HttpServletResponse response) {
    try {
      response.sendRedirect(buildAuthUri().toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void initializeAuth(String code, HttpServletResponse response) {
    Mono<String> jwtResponse = fetchJwt(code);
    String result = jwtResponse.block();
    setCookies(parseAccessToken(result), response);
  }

  @Override
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = jwtService.getRefreshTokenFromCookie(request);
    if (refreshToken == null) {
      throw new RuntimeException("No refresh token available");
    }

    Mono<String> refreshedTokenResponse =
        webClient
            .post()
            .uri(baseTokenUri)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(
                BodyInserters.fromFormData("grant_type", "refresh_token")
                    .with("client_id", clientId)
                    .with("client_secret", clientSecret)
                    .with("refresh_token", refreshToken))
            .retrieve()
            .bodyToMono(String.class);

    String result = refreshedTokenResponse.block();

    JwtValues jwtValues = parseAccessToken(result);
    setCookies(jwtValues, response);
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    clearCookie("access_token", response);
    clearCookie("access_token_exp", response);
    clearCookie("refresh_token", response);
    clearCookie("refresh_token_exp", response);

    String refreshToken = jwtService.getRefreshTokenFromCookie(request);
    if (refreshToken != null) {
      webClient
          .post()
          .uri(logoutUri)
          .header("Content-Type", "application/x-www-form-urlencoded")
          .body(
              BodyInserters.fromFormData("client_id", clientId).with("refresh_token", refreshToken))
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    }

    logger.info("User logged out successfully");
  }

  @Override
  public String getUserId(HttpServletRequest request) {
    String userId = jwtService.getUserIdFromJwt();

    return userId;
  }

  private void clearCookie(String name, HttpServletResponse response) {
    Cookie cookie = new Cookie(name, null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  private URI buildAuthUri() {
    return UriComponentsBuilder.fromHttpUrl(baseAuthUri)
        .queryParam("client_id", clientId)
        .queryParam("response_type", "code")
        .queryParam("scope", "openid profile")
        .queryParam("code_challenge_method", "S256")
        .queryParam("code_challenge", codeChallenge)
        .queryParam("redirect_uri", redirectUri)
        .build()
        .toUri();
  }

  private Mono<String> fetchJwt(String code) {
    String auth = clientId + ":" + clientSecret;
    byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("UTF-8")));
    String authHeader = "Basic " + new String(encodedAuth);

    return webClient
        .post()
        .uri(baseTokenUri)
        .header("Content-Type", "application/x-www-form-urlencoded")
        .header("Authorization", authHeader)
        .body(
            BodyInserters.fromFormData("grant_type", "authorization_code")
                .with("code", code)
                .with("redirect_uri", redirectUri)
                .with("code_verifier", codeVerifier))
        .retrieve()
        .bodyToMono(String.class);
  }

  private JwtValues parseAccessToken(String jwt) {
    JsonObject jsonObject = JsonParser.parseString(jwt).getAsJsonObject();
    String accessToken = jsonObject.get("access_token").getAsString();
    String refreshToken = jsonObject.get("refresh_token").getAsString();
    String accessExpiresIn = jsonObject.get("expires_in").getAsString();
    String refreshExpiresIn = jsonObject.get("refresh_expires_in").getAsString();
    String idToken = jsonObject.get("id_token").getAsString();
    String userId = jsonObject.get("sub").getAsString();

    return new JwtValues(
        accessToken, refreshToken, accessExpiresIn, refreshExpiresIn, idToken, userId);
  }

  private void setCookies(JwtValues jwtValues, HttpServletResponse response) {
    setAccessTokenCookie(jwtValues, response);
    setAccessTokenExpiryCookie(jwtValues, response);
    setRefreshTokenCookie(jwtValues, response);
    setRefreshTokenExpiryCookie(jwtValues, response);
  }

  private static void setAccessTokenCookie(JwtValues jwtValues, HttpServletResponse response) {
    Cookie accessTokenCookie = new Cookie("access_token", jwtValues.accessToken());
    accessTokenCookie.setHttpOnly(true);
    accessTokenCookie.setSecure(true);
    accessTokenCookie.setPath("/");
    accessTokenCookie.setMaxAge(Integer.parseInt(jwtValues.accessExpiresIn()));

    response.addCookie(accessTokenCookie);
  }

  private static void setAccessTokenExpiryCookie(
      JwtValues jwtValues, HttpServletResponse response) {
    long expirationTimeMillis =
        System.currentTimeMillis() + (Long.parseLong(jwtValues.accessExpiresIn()) * 1000L);

    Cookie expirationCookie = new Cookie("access_token_exp", String.valueOf(expirationTimeMillis));

    expirationCookie.setPath("/");
    expirationCookie.setMaxAge(Integer.parseInt(jwtValues.accessExpiresIn()));
    expirationCookie.setSecure(true);
    expirationCookie.setHttpOnly(false);

    response.addCookie(expirationCookie);
  }

  private static void setRefreshTokenCookie(JwtValues jwtValues, HttpServletResponse response) {
    Cookie cookie = new Cookie("refresh_token", jwtValues.refreshToken());
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(Integer.parseInt(jwtValues.accessExpiresIn()));

    response.addCookie(cookie);
  }

  private static void setRefreshTokenExpiryCookie(
      JwtValues jwtValues, HttpServletResponse response) {
    long expirationTimeMillis =
        System.currentTimeMillis() + (Long.parseLong(jwtValues.accessExpiresIn()) * 1000L);

    Cookie expirationCookie = new Cookie("refresh_token_exp", String.valueOf(expirationTimeMillis));

    expirationCookie.setPath("/");
    expirationCookie.setMaxAge(Integer.parseInt(jwtValues.refreshExpiresIn()));
    expirationCookie.setSecure(true);
    expirationCookie.setHttpOnly(false);

    response.addCookie(expirationCookie);
  }
}
