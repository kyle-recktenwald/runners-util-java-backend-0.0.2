package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO extends AbstractCRUDEntityDTO {

  private String roleId;

  private String name;

  private String description;

  private Set<String> permissions;
}
