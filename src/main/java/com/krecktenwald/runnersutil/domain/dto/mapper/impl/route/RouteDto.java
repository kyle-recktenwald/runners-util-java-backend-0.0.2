package com.krecktenwald.runnersutil.domain.dto.mapper.impl.route;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDto {
  private String routeId;

  private String name;

  private Double distance;

  private String userId;

  private CrudEntityInfoDto crudEntityInfo;
}
