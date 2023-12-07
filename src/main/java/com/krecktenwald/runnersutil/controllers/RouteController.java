package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RouteDTO;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.repositories.UserRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/routes")
public class RouteController {

  private final RouteRepository routeRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private DTOMapper dtoMapper;

  public RouteController(RouteRepository routeRepository) {
    this.routeRepository = routeRepository;
  }

  @GetMapping
  public Set<RouteDTO> getRoutes() {
    Set<RouteDTO> routeDTOs = new HashSet<>();
    for (Route route : routeRepository.findAll()) {
      routeDTOs.add(convertRouteToDTO(route));
    }

    return routeDTOs;
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
    route.setCreateDate(new Date());

    if (routeDTO.getRouteOwnerId() != null) {
      route.setRouteOwner(
          userRepository.findById(routeDTO.getRouteOwnerId()).orElseThrow(RuntimeException::new));
    }

    Route savedRoute = routeRepository.save(route);
    RouteDTO savedRouteDTO = convertRouteToDTO(savedRoute);
    savedRouteDTO.setRouteOwnerId(savedRoute.getRouteOwner().getUserId());

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
    if (routeDTO.getRouteOwnerId() != null) {
      existingRoute.setRouteOwner(
          userRepository.findById(routeDTO.getRouteOwnerId()).orElseThrow(RuntimeException::new));
    }

    existingRoute.setUpdateDate(new Date());

    return ResponseEntity.ok(convertRouteToDTO(routeRepository.save(existingRoute)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RouteDTO> deleteRoute(@PathVariable String id) {
    routeRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  private RouteDTO convertRouteToDTO(Route route) {
    RouteDTO routeDTO = dtoMapper.routeToRouteDTO(route);

    if (route.getRouteOwner() != null && route.getRouteOwner().getUserId() != null) {
      routeDTO.setRouteOwnerId(route.getRouteOwner().getUserId());
    }

    return routeDTO;
  }
}
