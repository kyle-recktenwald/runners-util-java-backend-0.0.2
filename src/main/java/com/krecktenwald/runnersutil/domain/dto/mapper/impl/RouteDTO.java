package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDTO extends AbstractCRUDEntityDTO {

  private String routeId;

  private String name;

  private Integer distance;

  private Set<String> runIds;

  private String routeOwnerId;
}
