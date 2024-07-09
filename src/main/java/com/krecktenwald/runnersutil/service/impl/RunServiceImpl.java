package com.krecktenwald.runnersutil.service.impl;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.CreateRunDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import com.krecktenwald.runnersutil.domain.entities.CrudEntityInfo;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.exceptions.EntityNotFoundException;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.repositories.RunRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import com.krecktenwald.runnersutil.service.RunService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RunServiceImpl implements RunService {

  private static final Logger logger = LogManager.getLogger(RunServiceImpl.class);

  private final RunRepository runRepository;
  private final DtoMapper dtoMapper;
  private final JwtService jwtService;
  private final RouteRepository routeRepository;

  @Autowired
  public RunServiceImpl(
      RunRepository runRepository,
      DtoMapper dtoMapper,
      JwtService jwtService,
      RouteRepository routeRepository) {
    this.runRepository = runRepository;
    this.dtoMapper = dtoMapper;
    this.jwtService = jwtService;
    this.routeRepository = routeRepository;
  }

  @Override
  public Set<RunDto> getRuns() {
    Set<RunDto> runDtos = new HashSet<>();
    List<Run> runs = runRepository.findAll();
    for (Run run : runs) {
      RunDto runDto = dtoMapper.runToRunDTO(run);
      runDto.setRoute(dtoMapper.routeToRouteDTO(run.getRoute()));
      runDtos.add(runDto);
    }

    return runDtos;
  }

  @Override
  public RunDto getRun(String id) {
    Run run = runRepository.findById(id).orElse(null);
    if (run == null) {
      String errorMessage = "Run with ID " + id + " not found";
      logger.error(errorMessage);
      throw new EntityNotFoundException(errorMessage);
    }

    return dtoMapper.runToRunDTO(run);
  }

  @Override
  public ResponseEntity<RunDto> createRun(CreateRunDto createRunDto) throws URISyntaxException {
    Run run = dtoMapper.map(createRunDto);
    run.setRunId(String.format("run_%s", UUID.randomUUID()));

    if (createRunDto.getUserId() != null) {
      run.setUserId(createRunDto.getUserId());
    }

    String creatorUserId = jwtService.getUserIdFromJwt();
    CrudEntityInfo crudEntityInfo = new CrudEntityInfo(creatorUserId);
    run.setCrudEntityInfo(crudEntityInfo);

    if (createRunDto.getRouteId() != null) {
      Route route = routeRepository.findById(createRunDto.getRouteId()).orElse(null);
      if (route != null) {
        run.setRoute(route);
      } else {
        logger.warn("Route does not exist. Run's route will be set to null.");
      }
    }

    Run createdRun = runRepository.save(run);
    RunDto createdRunDto = dtoMapper.runToRunDTO(createdRun);
    createdRunDto.setRoute(dtoMapper.routeToRouteDTO(createdRun.getRoute()));

    return ResponseEntity.created(new URI("/runs/" + createdRunDto.getRunId())).body(createdRunDto);
  }

  @Override
  public ResponseEntity<RunDto> updateRun(String id, CreateRunDto createRunDto) {
    Run existingRun = runRepository.findById(id).orElseThrow(RuntimeException::new);

    existingRun.setDistance(createRunDto.getDistance());
    existingRun.setStartDateTime(createRunDto.getStartDateTime());
    existingRun.setDuration(createRunDto.getDuration());
    existingRun.setUserId(createRunDto.getUserId());

    if (createRunDto.getRouteId() != null) {
      Route route = routeRepository.findById(createRunDto.getRouteId()).orElse(null);
      if (route != null) {
        existingRun.setRoute(route);
      } else {
        logger.warn(
            String.format(
                "Route with ID %s does not exist. Run's existing route will not be changed.",
                createRunDto.getRouteId()));
      }
    }

    existingRun.getCrudEntityInfo().setUpdateDate(new Date());
    String updaterUserId = jwtService.getUserIdFromJwt();
    if (updaterUserId != null) {
      existingRun.getCrudEntityInfo().setUpdatedBy(updaterUserId);
    } else {
      logger.error("No user found.");
      // TODO: Throw UserNotFoundException
    }

    Run updatedRun = runRepository.save(existingRun);

    RunDto updatedRunDto = dtoMapper.runToRunDTO(updatedRun);
    updatedRunDto.setRoute(dtoMapper.routeToRouteDTO(updatedRun.getRoute()));

    return ResponseEntity.ok(updatedRunDto);
  }

  @Override
  public ResponseEntity<RunDto> deleteRun(String id) {
    // TODO: Throw EntityNotFound Exception
    runRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
