package com.krecktenwald.runnersutil.domain.dto.mapper.impl.route;

import java.util.Set;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDto {

  private String routeId;

  private String name;

  private Double distance;

  private Set<RunDto> runs;

  private String userId;

  private CrudEntityInfoDto crudEntityInfo;
}
