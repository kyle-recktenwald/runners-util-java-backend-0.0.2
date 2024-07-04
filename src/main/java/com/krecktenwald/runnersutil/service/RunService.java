package com.krecktenwald.runnersutil.service;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.run.RunDto;

import java.util.Set;

public interface RunService {
    Set<RunDto> getRuns();
}
