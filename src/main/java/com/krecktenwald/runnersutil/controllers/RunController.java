package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CreateRunDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RunDto;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.repositories.RouteRepository;
import com.krecktenwald.runnersutil.repositories.RunRepository;
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

  @Autowired private RouteRepository routeRepository;

  @Autowired private DtoMapper dtoMapper;

  public RunController(RunRepository runRepository) {
    this.runRepository = runRepository;
  }

  @GetMapping
  @PreAuthorize("hasRole('ROLE_app_admin')")
  public Set<RunDto> getRuns() {
    Set<RunDto> runDtos = new HashSet<>();
    for (Run run : runRepository.findAll()) {
      runDtos.add(convertRunToDTO(run));
    }

    return runDtos;
  }

  @GetMapping("/{id}")
  public RunDto getRun(@PathVariable String id) {
    return convertRunToDTO(runRepository.findById(id).orElseThrow(RuntimeException::new));
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public ResponseEntity<RunDto> createRun(@RequestBody @Valid CreateRunDto createRunDto)
      throws URISyntaxException {
    Run run = new Run();
    run.setRunId(String.format("run_%s", UUID.randomUUID()));
    run.getCrudEntityInfo().setIsDeleted(false);

    if (createRunDto.getUserId() != null) {
      run.setUserId(createRunDto.getUserId());
    }

    if (createRunDto.getCreatedByUserId() != null) {
      run.getCrudEntityInfo().setCreatedBy(createRunDto.getCreatedByUserId());
    }

    if (createRunDto.getRouteId() != null) {
      Route route =
          routeRepository.findById(createRunDto.getRouteId()).orElseThrow(RuntimeException::new);
      run.setRoute(route);
    }

    if (createRunDto.getDuration() != null) {
      run.setDuration(createRunDto.getDuration());
    }

    if (createRunDto.getDistance() != null) {
      run.setDistance(createRunDto.getDistance());
    }

    if (createRunDto.getStartDateTime() != null) {
      run.setStartDateTime(createRunDto.getStartDateTime());
    }

    Run createdRun = runRepository.save(run);
    RunDto createdRunDto = dtoMapper.runToRunDTO(createdRun);

    // createdRunDTO.setRouteDto(createdRun.getRoute().getRouteId());

    return ResponseEntity.created(new URI("/runs/" + createdRunDto.getRunId())).body(createdRunDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RunDto> updateRun(@PathVariable String id, @RequestBody RunDto runDTO) {
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
      currentRun.setUserId(runDTO.getUserId());
    }

    /*if (runDTO.getRouteDto() != null) {
      Route route = routeRepository.findById(runDTO.getRunId()).orElseThrow(RuntimeException::new);
      currentRun.setRoute(route);
    }*/

    currentRun.getCrudEntityInfo().setUpdateDate(new Date());
    Run updatedRun = runRepository.save(currentRun);

    RunDto updatedRunDto = dtoMapper.runToRunDTO(updatedRun);
    // updatedRunDTO.setRouteDto(updatedRun.getRoute().getRouteId());

    return ResponseEntity.ok(updatedRunDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RunDto> deleteRun(@PathVariable String id) {
    runRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  private RunDto convertRunToDTO(Run run) {
    RunDto runDTO = dtoMapper.runToRunDTO(run);

    /*if (run.getRoute() != null && run.getRoute().getRouteId() != null) {
      runDTO.setRouteDto(run.getRoute().getRouteId());
    }*/

    return runDTO;
  }
}
