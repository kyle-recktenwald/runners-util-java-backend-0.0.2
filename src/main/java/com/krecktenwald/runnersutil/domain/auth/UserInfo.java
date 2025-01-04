package com.krecktenwald.runnersutil.domain.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserInfo {
  private final String userId;
  private final String username;
}
