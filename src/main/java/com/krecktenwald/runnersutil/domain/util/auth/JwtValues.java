package com.krecktenwald.runnersutil.domain.util.auth;

public record JwtValues(
    String accessToken,
    String refreshToken,
    String accessExpiresIn,
    String refreshExpiresIn,
    String idToken) {}
