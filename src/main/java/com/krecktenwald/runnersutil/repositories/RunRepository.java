package com.krecktenwald.runnersutil.repositories;

import com.krecktenwald.runnersutil.domain.entities.Run;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunRepository extends JpaRepository<Run, String> {}
