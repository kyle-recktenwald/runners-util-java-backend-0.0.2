package com.krecktenwald.runnersutil.controller;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.CreateRunDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import com.krecktenwald.runnersutil.service.RunService;
import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/runs")
public class RunController {
  private static final Logger logger = LogManager.getLogger(RunController.class);

  private final RunService runService;

  public RunController(RunService runService) {
    this.runService = runService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ROLE_app_admin')")
  public Set<RunDto> getRuns() {
    return runService.getRuns();
  }

  @PostMapping("/by-user")
  @PreAuthorize("hasRole('ROLE_app_admin') or @jwtService.getUserIdFromJwt() == #userId")
  public Set<RunDto> getRunsByUserId(@RequestParam String userId) {
    return runService.getRunsByUserId(userId);
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
    return runService.updateRun(id, createRunDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RunDto> deleteRun(@PathVariable String id) {
    return runService.deleteRun(id);
  }
}
