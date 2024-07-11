package com.krecktenwald.runnersutil.controller;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.CreateRouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import com.krecktenwald.runnersutil.service.RouteService;
import jakarta.validation.Valid;
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
  private final RouteService routeService;

  public RouteController(
      RouteRepository routeRepository,
      JwtService jwtService,
      DtoMapper dtoMapper,
      RouteService routeService) {
    this.routeRepository = routeRepository;
    this.jwtService = jwtService;
    this.dtoMapper = dtoMapper;
    this.routeService = routeService;
  }

  @GetMapping
  public Set<RouteDto> getRoutes() {
    return routeService.getRoutes();
  }

  @PostMapping("/by-user")
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public Set<RouteDto> getRoutesByUserId(@RequestParam String userId) {
    return routeService.getRoutesByUserId(userId);
  }

  @GetMapping("/{id}")
  public RouteDto getRoute(@PathVariable String id) {
    return routeService.getRoute(id);
  }

  @PostMapping
  public ResponseEntity<RouteDto> createRoute(@RequestBody @Valid CreateRouteDto createRouteDto)
      throws URISyntaxException {
    return routeService.createRoute(createRouteDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RouteDto> updateRoute(
      @PathVariable String id, @RequestBody @Valid CreateRouteDto createRouteDto) {
    return routeService.updateRoute(id, createRouteDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RouteDto> deleteRoute(@PathVariable String id) {
    return routeService.deleteRoute(id);
  }
}
