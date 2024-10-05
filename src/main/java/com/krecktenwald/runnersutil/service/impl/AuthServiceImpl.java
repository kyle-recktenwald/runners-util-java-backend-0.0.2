package com.krecktenwald.runnersutil.service.impl;

import com.krecktenwald.runnersutil.service.AuthService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.Cookie;
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
  private final String baseAuthUri;
  private final String baseTokenUri;
  private final String clientId;
  private final String clientSecret;
  private final String redirectUri;
  private final String codeChallenge;
  private final String codeVerifier;

  @Autowired
  public AuthServiceImpl(
      WebClient webClient,
      @Value("${keycloak.oauth2.auth-uri}") String baseAuthUri,
      @Value("${keycloak.oauth2.token-uri}") String baseTokenUri,
      @Value("${jwt.auth.converter.resource-id}") String clientId,
      @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
          String clientSecret,
      @Value("${keycloak.oauth2.redirect-uri}") String redirectUri,
      @Value("${keycloak.oauth2.code-challenge}") String codeChallenge,
      @Value("${keycloak.oauth2.code-verifier}") String codeVerifier) {
    this.webClient = webClient;
    this.baseAuthUri = baseAuthUri;
    this.baseTokenUri = baseTokenUri;
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
    setCookie(parseAccessToken(result), response);
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

  private String parseAccessToken(String jwt) {
    JsonObject jsonObject = JsonParser.parseString(jwt).getAsJsonObject();
    return jsonObject.get("access_token").getAsString();
  }

  private void setCookie(String jwt, HttpServletResponse response) {
    Cookie cookie = new Cookie("access_token", jwt);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(3600);

    String cookieHeaderValue =
        cookie.getName()
            + "="
            + cookie.getValue()
            + "; HttpOnly; Secure; Path=/; Max-Age="
            + cookie.getMaxAge()
            + "; SameSite=None";

    response.setHeader("Set-Cookie", cookieHeaderValue);
  }
}
