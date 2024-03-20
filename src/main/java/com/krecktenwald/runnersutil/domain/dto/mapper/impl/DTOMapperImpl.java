package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    runDTO.setCreateDate(run.getCreateDate());
    runDTO.setCreatedByUserId(run.getCreatedByUserId());
    runDTO.setUpdateDate(run.getUpdateDate());
    runDTO.setUpdatedByUserId(run.getUpdatedByUserId());
    runDTO.setIsDeleted(run.getIsDeleted());

    return runDTO;
  }

  @Override
  public Route routeDTOToRoute(RouteDTO routeDTO) {
    return null;
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
