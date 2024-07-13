package com.krecktenwald.runnersutil.repositories;

import com.krecktenwald.runnersutil.domain.entities.Run;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunRepository extends JpaRepository<Run, String> {
  List<Run> findAllByUserId(String userId);
}
