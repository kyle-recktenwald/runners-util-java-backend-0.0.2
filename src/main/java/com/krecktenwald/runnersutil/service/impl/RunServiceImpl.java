package com.krecktenwald.runnersutil.service.impl;

import com.krecktenwald.runnersutil.domain.dto.mapper.DtoMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.repositories.RunRepository;
import com.krecktenwald.runnersutil.service.RunService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

public class RunServiceImpl implements RunService {

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
}
