package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.CreateRouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.exceptions.EntityNotFoundException;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/routes")
public class RouteController {
  private static final Logger logger = LogManager.getLogger(RunController.class);

  private final RouteRepository routeRepository;
  private final JwtService jwtService;
  private final DtoMapper dtoMapper;

  public RouteController(
      RouteRepository routeRepository, JwtService jwtService, DtoMapper dtoMapper) {
    this.routeRepository = routeRepository;
    this.jwtService = jwtService;
    this.dtoMapper = dtoMapper;
  }

  @GetMapping
  public Set<RouteDto> getRoutes() {
    Set<RouteDto> routeDtos = new HashSet<>();
    for (Route route : routeRepository.findAll()) {
      routeDtos.add(dtoMapper.routeToRouteDTO(route));
    }

    return routeDtos;
  }

  @PostMapping("/by-user")
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public Set<RouteDto> getRoutesByUserId(@RequestParam String userId) {
    Set<RouteDto> routeDtos = new HashSet<>();
    for (Route route : routeRepository.findAllByUserId(userId)) {
      routeDtos.add(dtoMapper.routeToRouteDTO(route));
    }

    return routeDtos;
  }

  @GetMapping("/{id}")
  public RouteDto getRoute(@PathVariable String id) {
    Route route = routeRepository.findById(id).orElse(null);
    if (route == null) {
      String errorMessage = "Route with ID " + id + " not found";
      logger.error(errorMessage);
      throw new EntityNotFoundException(errorMessage);
    }

    return dtoMapper.routeToRouteDTO(route);
  }

  @PostMapping
  public ResponseEntity<RouteDto> createRoute(@RequestBody @Valid CreateRouteDto createRouteDto)
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

  @PutMapping("/{id}")
  public ResponseEntity<RouteDto> updateRoute(
      @PathVariable String id, @RequestBody @Valid CreateRouteDto createRouteDto) {
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

  @DeleteMapping("/{id}")
  public ResponseEntity<RouteDto> deleteRoute(@PathVariable String id) {
    // TODO: Throw EntityNotFound Exception
    routeRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
