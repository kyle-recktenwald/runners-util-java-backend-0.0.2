package com.krecktenwald.runnersutil.service.impl;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.CreateRouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.exceptions.EntityNotFoundException;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import com.krecktenwald.runnersutil.service.RouteService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class RouteServiceImpl implements RouteService {

  private static final Logger logger = LogManager.getLogger(RouteServiceImpl.class);

  private final RouteRepository routeRepository;
  private final DtoMapper dtoMapper;
  private final JwtService jwtService;

  @Autowired
  public RouteServiceImpl(
      RouteRepository routeRepository, DtoMapper dtoMapper, JwtService jwtService) {
    this.routeRepository = routeRepository;
    this.dtoMapper = dtoMapper;
    this.jwtService = jwtService;
  }

  @Override
  public Set<RouteDto> getRoutes() {
    Set<RouteDto> routeDtos = new HashSet<>();
    for (Route route : routeRepository.findAll()) {
      routeDtos.add(dtoMapper.routeToRouteDTO(route));
    }

    return routeDtos;
  }

  @Override
  public Set<RouteDto> getRoutesByUserId(String userId) {
    Set<RouteDto> routeDtos = new HashSet<>();
    for (Route route : routeRepository.findAllByUserId(userId)) {
      routeDtos.add(dtoMapper.routeToRouteDTO(route));
    }

    return routeDtos;
  }

  @Override
  public RouteDto getRoute(String id) {
    Route route = routeRepository.findById(id).orElse(null);
    if (route == null) {
      String errorMessage = "Route with ID " + id + " not found";
      logger.error(errorMessage);
      throw new EntityNotFoundException(errorMessage);
    }

    return dtoMapper.routeToRouteDTO(route);
  }

  @Override
  public ResponseEntity<RouteDto> createRoute(CreateRouteDto createRouteDto)
      throws URISyntaxException {
    Route route = dtoMapper.map(createRouteDto);
    route.setRouteId(String.format("route_%s", UUID.randomUUID()));

    String creatorUserId = jwtService.getUserIdFromJwt();
    CrudEntityInfo crudEntityInfo = new CrudEntityInfo(creatorUserId);
    route.setCrudEntityInfo(crudEntityInfo);

    if (createRouteDto.getUserId() != null) {
      route.setUserId(createRouteDto.getUserId());
    } else {
      route.setUserId(creatorUserId);
    }

    Route savedRoute = routeRepository.save(route);
    RouteDto savedRouteDto = dtoMapper.routeToRouteDTO(savedRoute);

    return ResponseEntity.created(new URI("/routes/" + savedRouteDto.getRouteId()))
        .body(savedRouteDto);
  }

  @Override
  public ResponseEntity<RouteDto> updateRoute(String id, CreateRouteDto createRouteDto) {
    Route existingRoute = routeRepository.findById(id).orElseThrow(RuntimeException::new);

    existingRoute.setName(createRouteDto.getName());
    existingRoute.setDistance(createRouteDto.getDistance());
    existingRoute.setUserId(createRouteDto.getUserId());

    existingRoute.getCrudEntityInfo().setUpdateDate(new Date());

    String creatorUserId = jwtService.getUserIdFromJwt();
    if (creatorUserId != null) {
      existingRoute.getCrudEntityInfo().setUpdatedBy(creatorUserId);
    } else {
      logger.error("No user found.");
      // TODO: Throw UserNotFoundException
    }

    return ResponseEntity.ok(dtoMapper.routeToRouteDTO(routeRepository.save(existingRoute)));
  }

  @Override
  public ResponseEntity<RouteDto> deleteRoute(String id) {
    // TODO: Throw EntityNotFound Exception
    routeRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
