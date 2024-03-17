package com.krecktenwald.runnersutil.repositories;

import com.krecktenwald.runnersutil.domain.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, String> {
    List<Route> findAllByUserId(String userId);
}
