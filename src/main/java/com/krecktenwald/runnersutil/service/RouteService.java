package com.krecktenwald.runnersutil.service;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.CreateRouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import java.net.URISyntaxException;
import java.util.Set;
import org.springframework.http.ResponseEntity;

public interface RouteService {
  Set<RouteDto> getRoutes();

  Set<RouteDto> getRoutesByUserId(String userId);

  RouteDto getRoute(String id);

  ResponseEntity<RouteDto> createRoute(CreateRouteDto createRouteDto) throws URISyntaxException;

  ResponseEntity<RouteDto> updateRoute(String id, CreateRouteDto createRouteDto);

  ResponseEntity<RouteDto> deleteRoute(String id);
}
