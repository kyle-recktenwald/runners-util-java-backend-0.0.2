package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends AbstractCRUDEntityDTO {
  private String userId;

  private String authId;

  private String emailAddress;

  private Set<RoleDTO> roles;

  private Set<String> runs;

  private Set<String> routes;
}
