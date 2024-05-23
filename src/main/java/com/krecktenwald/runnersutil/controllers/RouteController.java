package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RouteDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/routes")
public class RouteController {
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
      routeDtos.add(convertRouteToDTO(route));
    }

    return routeDtos;
  }

  @PostMapping("/by-user")
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public Set<RouteDto> getRoutesByUserId(@RequestParam String userId) {
    Set<RouteDto> routeDtos = new HashSet<>();
    for (Route route : routeRepository.findAllByUserId(userId)) {
      routeDtos.add(convertRouteToDTO(route));
    }

    return routeDtos;
  }

  @GetMapping("/{id}")
  public RouteDto getRoute(@PathVariable String id) {
    return convertRouteToDTO(routeRepository.findById(id).orElseThrow(RuntimeException::new));
  }

  @PostMapping
  public ResponseEntity<RouteDto> createRoute(@RequestBody @Valid RouteDto routeDTO)
      throws URISyntaxException {
    Route route = dtoMapper.routeDTOToRoute(routeDTO);
    route.setRouteId(String.format("route_%s", UUID.randomUUID()));

    String creatorUserId = jwtService.getUserIdFromJwt();
    CrudEntityInfo crudEntityInfo = new CrudEntityInfo(creatorUserId);
    route.setCrudEntityInfo(crudEntityInfo);

    if (routeDTO.getUserId() != null) {
      route.setUserId(routeDTO.getUserId());
    } else {
      route.setUserId(creatorUserId);
    }

    Route savedRoute = routeRepository.save(route);
    RouteDto savedRouteDto = convertRouteToDTO(savedRoute);

    return ResponseEntity.created(new URI("/routes/" + savedRouteDto.getRouteId()))
        .body(savedRouteDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RouteDto> updateRoute(
      @PathVariable String id, @RequestBody RouteDto routeDTO) {
    Route existingRoute = routeRepository.findById(id).orElseThrow(RuntimeException::new);

    if (routeDTO.getName() != null) {
      existingRoute.setName(routeDTO.getName());
    }
    if (routeDTO.getDistance() != null) {
      existingRoute.setDistance(routeDTO.getDistance());
    }
    if (routeDTO.getUserId() != null) {
      existingRoute.setUserId(routeDTO.getUserId());
    }

    existingRoute.getCrudEntityInfo().setUpdateDate(new Date());
    String creatorUserId = jwtService.getUserIdFromJwt();
    if (creatorUserId != null) {
      existingRoute.getCrudEntityInfo().setUpdatedBy(creatorUserId);
    }

    return ResponseEntity.ok(convertRouteToDTO(routeRepository.save(existingRoute)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RouteDto> deleteRoute(@PathVariable String id) {
    routeRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  private RouteDto convertRouteToDTO(Route route) {
    return dtoMapper.routeToRouteDTO(route);
  }
}
