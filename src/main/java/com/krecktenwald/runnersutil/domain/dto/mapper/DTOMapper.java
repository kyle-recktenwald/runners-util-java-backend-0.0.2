package com.krecktenwald.runnersutil.domain.dto.mapper;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RunDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

/** * Maps POJO/Entity objects to Data Transfer Objects (DTOs), and vice-versa */
@Mapper(componentModel = "spring")
public interface DTOMapper {
  RouteDto routeToRouteDTO(Route route);

  RunDto runToRunDTO(Run run);

  Route routeDTOToRoute(RouteDto routeDTO);

  CrudEntityInfoDto map(CrudEntityInfo crudEntityInfo);

  CrudEntityInfo map(CrudEntityInfoDto crudEntityInfoDto);

  Run runDTOToRun(RunDto runDTO);

  Set<RouteDto> routesToRouteDTOs(Set<Route> routes);

  Set<RunDto> runsToRunDTOs(Set<Run> runs);

  Set<Route> routeDTOsToRoutes(Set<RouteDto> routeDtos);

  Set<Run> runDTOsToRuns(Set<RunDto> runDtos);

  List<RouteDto> routesToRouteDTOs(List<Route> routes);

  List<RunDto> runsToRunDTOs(List<Run> runs);

  List<Route> routeDTOsToRoutes(List<RouteDto> routeDtos);

  List<Run> runDTOsToRuns(List<RunDto> runDtos);
}
