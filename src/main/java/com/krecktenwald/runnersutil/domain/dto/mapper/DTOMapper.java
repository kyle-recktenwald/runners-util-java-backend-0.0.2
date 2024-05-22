package com.krecktenwald.runnersutil.domain.dto.mapper;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RouteDTO;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RunDTO;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

/** * Maps POJO/Entity objects to Data Transfer Objects (DTOs), and vice-versa */
@Mapper(componentModel = "spring")
public interface DTOMapper {
  RouteDTO routeToRouteDTO(Route route);

  RunDTO runToRunDTO(Run run);

  Route routeDTOToRoute(RouteDTO routeDTO);

  CrudEntityInfoDto map(CrudEntityInfo crudEntityInfo);

  CrudEntityInfo map(CrudEntityInfoDto crudEntityInfoDto);

  Run runDTOToRun(RunDTO runDTO);

  Set<RouteDTO> routesToRouteDTOs(Set<Route> routes);

  Set<RunDTO> runsToRunDTOs(Set<Run> runs);

  Set<Route> routeDTOsToRoutes(Set<RouteDTO> routeDTOs);

  Set<Run> runDTOsToRuns(Set<RunDTO> runDTOs);

  List<RouteDTO> routesToRouteDTOs(List<Route> routes);

  List<RunDTO> runsToRunDTOs(List<Run> runs);

  List<Route> routeDTOsToRoutes(List<RouteDTO> routeDTOs);

  List<Run> runDTOsToRuns(List<RunDTO> runDTOs);
}
