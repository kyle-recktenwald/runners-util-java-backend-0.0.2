package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDTO {

  private String routeId;

  private String name;

  private Double distance;

  private Set<RunDTO> runs;

  private String userId;

  private CrudEntityInfoDto crudEntityInfo;
}
