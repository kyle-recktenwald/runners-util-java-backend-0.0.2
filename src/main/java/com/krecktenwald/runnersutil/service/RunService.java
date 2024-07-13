package com.krecktenwald.runnersutil.service;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.CreateRunDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;
import java.net.URISyntaxException;
import java.util.Set;
import org.springframework.http.ResponseEntity;

public interface RunService {
  Set<RunDto> getRuns();

  Set<RunDto> getRunsByUserId(String userId);

  RunDto getRun(String id);

  ResponseEntity<RunDto> createRun(CreateRunDto createRunDto) throws URISyntaxException;

  ResponseEntity<RunDto> updateRun(String id, CreateRunDto createRunDto);

  ResponseEntity<RunDto> deleteRun(String id);
}
