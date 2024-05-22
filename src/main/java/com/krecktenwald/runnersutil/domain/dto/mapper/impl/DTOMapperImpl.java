package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class DTOMapperImpl implements DTOMapper {
  @Override
  public RouteDTO routeToRouteDTO(Route route) {
    RouteDTO routeDTO = new RouteDTO();
    routeDTO.setUserId(route.getUserId());
    routeDTO.setRouteId(route.getRouteId());
    routeDTO.setName(route.getName());
    routeDTO.setDistance(route.getDistance());

    return routeDTO;
  }

  @Override
  public RunDTO runToRunDTO(Run run) {
    RunDTO runDTO = new RunDTO();
    runDTO.setRunId(run.getRunId());
    runDTO.setUserId(run.getUserId());
    runDTO.setDistance(run.getDistance());
    runDTO.setStartDateTime(run.getStartDateTime());
    runDTO.setDuration(run.getDuration());
    runDTO.getCrudEntityInfo().setCreateDate(run.getCrudEntityInfo().getCreateDate());
    runDTO.getCrudEntityInfo().setCreatedBy(run.getCrudEntityInfo().getCreatedBy());
    runDTO.getCrudEntityInfo().setUpdateDate(run.getCrudEntityInfo().getUpdateDate());
    runDTO.getCrudEntityInfo().setUpdatedBy(run.getCrudEntityInfo().getUpdatedBy());
    runDTO.getCrudEntityInfo().setIsDeleted(run.getCrudEntityInfo().getIsDeleted());

    return runDTO;
  }

  @Override
  public Route routeDTOToRoute(RouteDTO routeDTO) {
    Route route = new Route();
    route.setRouteId(routeDTO.getRouteId());
    route.setName(routeDTO.getName());
    route.setDistance(routeDTO.getDistance());
    route.setUserId(routeDTO.getUserId());

    if (routeDTO.getCrudEntityInfo() != null) {
      CrudEntityInfoDto crudEntityInfoDto = routeDTO.getCrudEntityInfo();
      CrudEntityInfo crudEntityInfo = new CrudEntityInfo();
      crudEntityInfo.setCreateDate(crudEntityInfoDto.getCreateDate());
      crudEntityInfo.setUpdateDate(crudEntityInfoDto.getUpdateDate());
      crudEntityInfo.setCreatedBy(crudEntityInfoDto.getCreatedBy());
      crudEntityInfo.setUpdatedBy(crudEntityInfoDto.getUpdatedBy());
      crudEntityInfo.setIsDeleted(crudEntityInfoDto.getIsDeleted());
      route.setCrudEntityInfo(crudEntityInfo);
    }

    return route;
  }

  @Override
  public Run runDTOToRun(RunDTO runDTO) {
    return null;
  }

  @Override
  public Set<RouteDTO> routesToRouteDTOs(Set<Route> routes) {
    return null;
  }

  @Override
  public Set<RunDTO> runsToRunDTOs(Set<Run> runs) {
    return null;
  }

  @Override
  public Set<Route> routeDTOsToRoutes(Set<RouteDTO> routeDTOs) {
    return null;
  }

  @Override
  public Set<Run> runDTOsToRuns(Set<RunDTO> runDTOs) {
    return null;
  }

  @Override
  public List<RouteDTO> routesToRouteDTOs(List<Route> routes) {
    return null;
  }

  @Override
  public List<RunDTO> runsToRunDTOs(List<Run> runs) {
    return null;
  }

  @Override
  public List<Route> routeDTOsToRoutes(List<RouteDTO> routeDTOs) {
    return null;
  }

  @Override
  public List<Run> runDTOsToRuns(List<RunDTO> runDTOs) {
    return null;
  }
}
