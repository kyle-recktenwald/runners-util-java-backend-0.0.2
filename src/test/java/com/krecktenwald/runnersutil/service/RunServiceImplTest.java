package com.krecktenwald.runnersutil.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.CreateRunDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.exceptions.EntityNotFoundException;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.repositories.RunRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import java.net.URISyntaxException;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RunServiceImplTest {

  private static final String RUN_WITH_ROUTE_ID = "run_6d43fe7f-a85f-4b53-a074-b15116cea51b";
  private static final String RUN_WITH_NO_ROUTE_ID = "route_1dbfc1a8-e7e5-43ac-a975-438f350b6963";
  public static final String ROUTE_ID = "route_1dbfc1a8-e7e5-43ac-a975-438f350b6963";
  public static final String EXISTING_RUN_ID = "run_4c2898b1-3087-4260-8406-6591a11794bb";
  public static final String USER_ID = "a11226da-a3c8-41c1-bce7-83fbcfb68ca8";

  @Mock private RunRepository runRepository;

  @Mock private DtoMapper dtoMapper;

  @Mock private JwtService jwtService;

  @Mock private RouteRepository routeRepository;

  @InjectMocks private RunServiceImpl runService;

  private Run runWithRoute;
  private RunDto runWithRouteDto;
  private Run runWithNoRoute;
  private RunDto runWithNoRouteDto;
  private CreateRunDto createRunDto;
  private Route route;
  private RouteDto routeDto;

  private Run existingRun;
  private RunDto existingRunDto;

  @BeforeEach
  void setUp() {
    route = new Route();
    route.setRouteId(ROUTE_ID);

    routeDto = new RouteDto();
    routeDto.setRouteId(ROUTE_ID);

    runWithRoute = new Run();
    runWithRoute.setRunId(RUN_WITH_ROUTE_ID);
    runWithRoute.setRoute(route);

    runWithRouteDto = new RunDto();
    runWithRouteDto.setRunId(RUN_WITH_ROUTE_ID);
    runWithRouteDto.setRoute(routeDto);

    runWithNoRoute = new Run();
    runWithNoRoute.setRunId(RUN_WITH_NO_ROUTE_ID);

    runWithNoRouteDto = new RunDto();
    runWithNoRouteDto.setRunId(RUN_WITH_NO_ROUTE_ID);

    createRunDto = new CreateRunDto();
    createRunDto.setRouteId(ROUTE_ID);

    existingRun = new Run();
    existingRun.setRunId(EXISTING_RUN_ID);
    existingRun.setRoute(route);
    existingRun.setCrudEntityInfo(new CrudEntityInfo());

    existingRunDto = new RunDto();
    existingRunDto.setRunId(EXISTING_RUN_ID);
    existingRunDto.setRoute(routeDto);
    existingRunDto.setCrudEntityInfo(new CrudEntityInfoDto());
  }

  @Test
  void testGetRuns() {
    List<Run> runs = new ArrayList<>();
    runs.add(runWithRoute);
    runs.add(runWithNoRoute);

    when(runRepository.findAll()).thenReturn(runs);
    when(dtoMapper.runToRunDTO(runWithRoute)).thenReturn(runWithRouteDto);
    when(dtoMapper.routeToRouteDTO(route)).thenReturn(routeDto);
    when(dtoMapper.runToRunDTO(runWithNoRoute)).thenReturn(runWithNoRouteDto);

    Set<RunDto> result = runService.getRuns();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(2, result.size());

    verify(runRepository, times(1)).findAll();
    verify(dtoMapper, times(1)).runToRunDTO(runWithRoute);
    verify(dtoMapper, times(1)).routeToRouteDTO(route);
    verify(dtoMapper, times(1)).runToRunDTO(runWithNoRoute);
  }

  @Test
  void testGetRun() {
    when(runRepository.findById(anyString())).thenReturn(Optional.of(runWithRoute));
    when(dtoMapper.runToRunDTO(runWithRoute)).thenReturn(runWithRouteDto);

    RunDto result = runService.getRun(RUN_WITH_ROUTE_ID);

    assertNotNull(result);
    assertEquals(RUN_WITH_ROUTE_ID, result.getRunId());

    verify(runRepository, times(1)).findById(RUN_WITH_ROUTE_ID);
    verify(dtoMapper, times(1)).runToRunDTO(runWithRoute);
  }

  @Test
  void testGetRun_RunNotFound() {
    when(runRepository.findById(anyString())).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(EntityNotFoundException.class, () -> runService.getRun(RUN_WITH_ROUTE_ID));

    assertEquals(
        String.format("Run with ID %s not found", RUN_WITH_ROUTE_ID), exception.getMessage());

    verify(runRepository, times(1)).findById(RUN_WITH_ROUTE_ID);
    verify(dtoMapper, times(0)).runToRunDTO(runWithRoute);
  }

  @Test
  void testCreateRun() throws URISyntaxException {
    when(dtoMapper.map(createRunDto)).thenReturn(runWithRoute);
    when(jwtService.getUserIdFromJwt()).thenReturn(USER_ID);
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(route));
    when(runRepository.save(runWithRoute)).thenReturn(runWithRoute);
    when(dtoMapper.runToRunDTO(runWithRoute)).thenReturn(runWithRouteDto);
    when(dtoMapper.routeToRouteDTO(route)).thenReturn(routeDto);

    ResponseEntity<RunDto> result = runService.createRun(createRunDto);

    assertNotNull(result);
    assertEquals(201, result.getStatusCode().value());
    assertNotNull(result.getBody());
    assertEquals(RUN_WITH_ROUTE_ID, result.getBody().getRunId());

    verify(dtoMapper, times(1)).map(createRunDto);
    verify(jwtService, times(1)).getUserIdFromJwt();
    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(runRepository, times(1)).save(runWithRoute);
    verify(dtoMapper, times(1)).runToRunDTO(runWithRoute);
    verify(dtoMapper, times(1)).routeToRouteDTO(route);
  }

  @Test
  void testUpdateRun() {
    when(runRepository.findById(EXISTING_RUN_ID)).thenReturn(Optional.of(existingRun));
    when(jwtService.getUserIdFromJwt()).thenReturn(USER_ID);
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(route));
    when(runRepository.save(existingRun)).thenReturn(existingRun);
    when(dtoMapper.runToRunDTO(existingRun)).thenReturn(existingRunDto);
    when(dtoMapper.routeToRouteDTO(route)).thenReturn(routeDto);

    ResponseEntity<RunDto> result = runService.updateRun(EXISTING_RUN_ID, createRunDto);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());
    assertNotNull(result.getBody());
    assertEquals(EXISTING_RUN_ID, result.getBody().getRunId());

    verify(runRepository, times(1)).findById(EXISTING_RUN_ID);
    verify(jwtService, times(1)).getUserIdFromJwt();
    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(runRepository, times(1)).save(existingRun);
    verify(dtoMapper, times(1)).runToRunDTO(existingRun);
    verify(dtoMapper, times(1)).routeToRouteDTO(route);
  }

  @Test
  void testDeleteRun() {
    doNothing().when(runRepository).deleteById(anyString());

    ResponseEntity<RunDto> result = runService.deleteRun(RUN_WITH_ROUTE_ID);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());

    verify(runRepository, times(1)).deleteById(RUN_WITH_ROUTE_ID);
  }
}
