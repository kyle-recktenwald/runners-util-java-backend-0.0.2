package com.krecktenwald.runnersutil.controller;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.CreateRunDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.repositories.RunRepository;
import com.krecktenwald.runnersutil.security.JwtService;
import com.krecktenwald.runnersutil.service.RunService;
import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/runs")
public class RunController {
  private static final Logger logger = LogManager.getLogger(RunController.class);

  private final RunRepository runRepository;
  private final RouteRepository routeRepository;
  private final DtoMapper dtoMapper;
  private final JwtService jwtService;
  private final RunService runService;

  public RunController(
      RunRepository runRepository,
      RouteRepository routeRepository,
      DtoMapper dtoMapper,
      JwtService jwtService,
      RunService runService) {
    this.runRepository = runRepository;
    this.routeRepository = routeRepository;
    this.dtoMapper = dtoMapper;
    this.jwtService = jwtService;
    this.runService = runService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ROLE_app_admin')")
  public Set<RunDto> getRuns() {
    return runService.getRuns();
  }

  @GetMapping("/{id}")
  public RunDto getRun(@PathVariable String id) {
    return runService.getRun(id);
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public ResponseEntity<RunDto> createRun(@RequestBody @Valid CreateRunDto createRunDto)
      throws URISyntaxException {
    return runService.createRun(createRunDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RunDto> updateRun(
      @PathVariable String id, @RequestBody @Valid CreateRunDto createRunDto) {
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

  @DeleteMapping("/{id}")
  public ResponseEntity<RunDto> deleteRun(@PathVariable String id) {
    // TODO: Throw EntityNotFound Exception
    runRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
