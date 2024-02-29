package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RunDTO;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.domain.entities.User;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.repositories.RunRepository;
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
import org.springframework.security.access.prepost.PostAuthorize;
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

  private final RunRepository runRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private RouteRepository routeRepository;

  @Autowired DTOMapper dtoMapper;

  public RunController(RunRepository runRepository) {
    this.runRepository = runRepository;
  }

  @GetMapping
  public Set<RunDTO> getRuns() {
    Set<RunDTO> runDTOs = new HashSet<>();
    for (Run run : runRepository.findAll()) {
      runDTOs.add(convertRunToDTO(run));
    }

    return runDTOs;
  }

  @GetMapping("/{id}")
  public RunDTO getRun(@PathVariable String id) {
    return convertRunToDTO(runRepository.findById(id).orElseThrow(RuntimeException::new));
  }

  @PostMapping
  public ResponseEntity<RunDTO> createRun(@RequestBody @Valid RunDTO runDTO)
      throws URISyntaxException {
    Run run = dtoMapper.runDTOToRun(runDTO);
    run.setRunId(String.format("run_%s", UUID.randomUUID()));
    run.setCreateDate(new Date());

    if (runDTO.getUserId() != null) {
      User user = userRepository.findById(runDTO.getUserId()).orElseThrow(RuntimeException::new);
      run.setRunOwner(user);
    }

    if (runDTO.getRouteId() != null) {
      Route route =
          routeRepository.findById(runDTO.getRouteId()).orElseThrow(RuntimeException::new);
      run.setRoute(route);
    }

    Run createdRun = runRepository.save(run);
    RunDTO createdRunDTO = dtoMapper.runToRunDTO(createdRun);

    createdRunDTO.setUserId(createdRun.getRunOwner().getUserId());
    createdRunDTO.setRouteId(createdRun.getRoute().getRouteId());

    return ResponseEntity.created(new URI("/runs/" + createdRunDTO.getRunId())).body(createdRunDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RunDTO> updateRun(@PathVariable String id, @RequestBody RunDTO runDTO) {
    Run currentRun = runRepository.findById(id).orElseThrow(RuntimeException::new);

    if (runDTO.getDistance() != null) {
      currentRun.setDistance(runDTO.getDistance());
    }

    if (runDTO.getStartDateTime() != null) {
      currentRun.setStartDateTime(runDTO.getStartDateTime());
    }

    if (runDTO.getDuration() != null) {
      currentRun.setDuration(runDTO.getDuration());
    }

    if (runDTO.getUserId() != null) {
      User user = userRepository.findById(runDTO.getUserId()).orElseThrow(RuntimeException::new);
      currentRun.setRunOwner(user);
    }

    if (runDTO.getRouteId() != null) {
      Route route = routeRepository.findById(runDTO.getRunId()).orElseThrow(RuntimeException::new);
      currentRun.setRoute(route);
    }

    currentRun.setUpdateDate(new Date());
    Run updatedRun = runRepository.save(currentRun);

    RunDTO updatedRunDTO = dtoMapper.runToRunDTO(updatedRun);

    updatedRunDTO.setUserId(updatedRun.getRunOwner().getUserId());
    updatedRunDTO.setRouteId(updatedRun.getRoute().getRouteId());

    return ResponseEntity.ok(updatedRunDTO);
  }

  // @Secured("ROLE_ADMIN")
  @PostAuthorize("hasRole('ADMIN') or #id == authentication.name")
  @DeleteMapping("/{id}")
  public ResponseEntity<RunDTO> deleteRun(@PathVariable String id) {
    runRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  private RunDTO convertRunToDTO(Run run) {
    RunDTO runDTO = dtoMapper.runToRunDTO(run);

    if (run.getRunOwner() != null && run.getRunOwner().getUserId() != null) {
      runDTO.setUserId(run.getRunOwner().getUserId());
    }

    if (run.getRoute() != null && run.getRoute().getRouteId() != null) {
      runDTO.setRouteId(run.getRoute().getRouteId());
    }

    return runDTO;
  }
}
