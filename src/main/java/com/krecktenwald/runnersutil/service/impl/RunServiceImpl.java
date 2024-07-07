package com.krecktenwald.runnersutil.service.impl;

import com.krecktenwald.runnersutil.controller.RunController;
import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.exceptions.EntityNotFoundException;
import com.krecktenwald.runnersutil.repositories.RunRepository;
import com.krecktenwald.runnersutil.service.RunService;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunServiceImpl implements RunService {

  private static final Logger logger = LogManager.getLogger(RunController.class);

  private final RunRepository runRepository;
  private final DtoMapper dtoMapper;

  @Autowired
  public RunServiceImpl(RunRepository runRepository, DtoMapper dtoMapper) {
    this.runRepository = runRepository;
    this.dtoMapper = dtoMapper;
  }

  @Override
  public Set<RunDto> getRuns() {
    Set<RunDto> runDtos = new HashSet<>();
    for (Run run : runRepository.findAll()) {
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
}
