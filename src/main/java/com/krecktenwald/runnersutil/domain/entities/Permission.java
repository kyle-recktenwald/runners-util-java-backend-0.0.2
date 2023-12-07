package com.krecktenwald.runnersutil.domain.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
  CAN_VIEW_ADMIN_CONSOLE("Can View Admin Console", "User can view the administrator console."),
  CAN_VIEW_SUPER_ADMIN_CONSOLE(
      "Can View Super Admin Console", "User can view the super administrator console.");

  private final String name;
  private final String description;
}
