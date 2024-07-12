package com.krecktenwald.runnersutil.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.CreateRouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.exceptions.EntityNotFoundException;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import com.krecktenwald.runnersutil.service.impl.RouteServiceImpl;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

  public static final String ROUTE_ID = "route_1dbfc1a8-e7e5-43ac-a975-438f350b6963";
  public static final String EXISTING_ROUTE_ID = "route_935b8c6b-4cea-45fe-982c-413511105385";
  public static final String USER_ID = "a11226da-a3c8-41c1-bce7-83fbcfb68ca8";

  @Mock private RouteRepository routeRepository;
  @Mock private DtoMapper dtoMapper;
  @Mock private JwtService jwtService;

  @InjectMocks private RouteServiceImpl routeService;

  private Route route;
  private RouteDto routeDto;
  private CreateRouteDto createRouteDto;

  private Route existingRoute;
  private RouteDto existingRouteDto;

  @BeforeEach
  void setUp() {
    route = new Route();
    route.setRouteId(ROUTE_ID);

    routeDto = new RouteDto();
    routeDto.setRouteId(ROUTE_ID);

    createRouteDto = new CreateRouteDto();

    existingRoute = new Route();
    existingRoute.setRouteId(ROUTE_ID);
    existingRoute.setCrudEntityInfo(new CrudEntityInfo());

    existingRouteDto = new RouteDto();
    existingRouteDto.setRouteId(EXISTING_ROUTE_ID);
    existingRouteDto.setCrudEntityInfo(new CrudEntityInfoDto());
  }

  @Test
  void testGetRoutes() {
    List<Route> routes = new ArrayList<>();
    routes.add(route);

    when(routeRepository.findAll()).thenReturn(routes);
    when(dtoMapper.routeToRouteDTO(route)).thenReturn(routeDto);

    Set<RouteDto> result = routeService.getRoutes();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());

    verify(routeRepository, times(1)).findAll();
    verify(dtoMapper, times(1)).routeToRouteDTO(route);
  }

  @Test
  void testGetRoute() {
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(route));
    when(dtoMapper.routeToRouteDTO(route)).thenReturn(routeDto);

    RouteDto result = routeService.getRoute(ROUTE_ID);

    assertNotNull(result);
    assertEquals(ROUTE_ID, result.getRouteId());

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(dtoMapper, times(1)).routeToRouteDTO(route);
  }

  @Test
  void testGetRoute_RouteNotFound() {
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(EntityNotFoundException.class, () -> routeService.getRoute(ROUTE_ID));

    assertEquals(String.format("Route with ID %s not found", ROUTE_ID), exception.getMessage());

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(dtoMapper, times(0)).routeToRouteDTO(route);
  }

  @Test
  void testCreateRoute() throws URISyntaxException {
    when(dtoMapper.map(createRouteDto)).thenReturn(route);
    when(jwtService.getUserIdFromJwt()).thenReturn(USER_ID);
    when(routeRepository.save(route)).thenReturn(route);
    when(dtoMapper.routeToRouteDTO(route)).thenReturn(routeDto);

    ResponseEntity<RouteDto> result = routeService.createRoute(createRouteDto);

    assertNotNull(result);
    assertEquals(201, result.getStatusCode().value());
    assertNotNull(result.getBody());
    assertEquals(ROUTE_ID, result.getBody().getRouteId());

    verify(dtoMapper, times(1)).map(createRouteDto);
    verify(jwtService, times(1)).getUserIdFromJwt();
    verify(routeRepository, times(1)).save(route);
    verify(dtoMapper, times(1)).routeToRouteDTO(route);
  }

  @Test
  void testUpdateRun() {
    when(routeRepository.findById(EXISTING_ROUTE_ID)).thenReturn(Optional.of(existingRoute));
    when(jwtService.getUserIdFromJwt()).thenReturn(USER_ID);
    when(routeRepository.save(existingRoute)).thenReturn(existingRoute);
    when(dtoMapper.routeToRouteDTO(existingRoute)).thenReturn(existingRouteDto);

    ResponseEntity<RouteDto> result = routeService.updateRoute(EXISTING_ROUTE_ID, createRouteDto);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());
    assertNotNull(result.getBody());
    assertEquals(EXISTING_ROUTE_ID, result.getBody().getRouteId());

    verify(routeRepository, times(1)).findById(EXISTING_ROUTE_ID);
    verify(jwtService, times(1)).getUserIdFromJwt();
    verify(routeRepository, times(1)).save(existingRoute);
    verify(dtoMapper, times(1)).routeToRouteDTO(existingRoute);
  }

  @Test
  void testDeleteRun() {
    doNothing().when(routeRepository).deleteById(anyString());

    ResponseEntity<RouteDto> result = routeService.deleteRoute(ROUTE_ID);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());

    verify(routeRepository, times(1)).deleteById(ROUTE_ID);
  }
}
