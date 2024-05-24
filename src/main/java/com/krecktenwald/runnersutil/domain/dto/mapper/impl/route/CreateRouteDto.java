package com.krecktenwald.runnersutil.domain.dto.mapper.impl.route;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRouteDto {
  @NotNull private String name;

  @NotNull private Double distance;

  @NotNull private String userId;
}
