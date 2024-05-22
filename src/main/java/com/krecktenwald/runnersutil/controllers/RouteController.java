package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RouteDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/routes")
public class RouteController {

  private final RouteRepository routeRepository;
  private final JwtService jwtService;

  @Autowired private DTOMapper dtoMapper;

  public RouteController(RouteRepository routeRepository, JwtService jwtService) {
    this.routeRepository = routeRepository;
    this.jwtService = jwtService;
  }

  @GetMapping
  public Set<RouteDTO> getRoutes() {
    Set<RouteDTO> routeDTOs = new HashSet<>();
    for (Route route : routeRepository.findAll()) {
      routeDTOs.add(convertRouteToDTO(route));
    }

    return routeDTOs;
  }

  @PostMapping("/by-user")
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public Set<RouteDTO> getRoutesByUserId(@RequestParam String userId) {
    Set<RouteDTO> routeDtos = new HashSet<>();
    for (Route route : routeRepository.findAllByUserId(userId)) {
      routeDtos.add(convertRouteToDTO(route));
    }

    return routeDtos;
  }

  @GetMapping("/{id}")
  public RouteDTO getRoute(@PathVariable String id) {
    return convertRouteToDTO(routeRepository.findById(id).orElseThrow(RuntimeException::new));
  }

  @PostMapping
  public ResponseEntity<RouteDTO> createRoute(@RequestBody @Valid RouteDTO routeDTO)
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
    RouteDTO savedRouteDTO = convertRouteToDTO(savedRoute);

    return ResponseEntity.created(new URI("/routes/" + savedRouteDTO.getRouteId()))
        .body(savedRouteDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RouteDTO> updateRoute(
      @PathVariable String id, @RequestBody RouteDTO routeDTO) {
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

    return ResponseEntity.ok(convertRouteToDTO(routeRepository.save(existingRoute)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RouteDTO> deleteRoute(@PathVariable String id) {
    routeRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  private RouteDTO convertRouteToDTO(Route route) {
    return dtoMapper.routeToRouteDTO(route);
  }
}
