package com.krecktenwald.runnersutil.repositories;

import com.krecktenwald.runnersutil.domain.entities.Route;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, String> {
  List<Route> findAllByUserId(String userId);
}
