package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RunDTO;
import com.krecktenwald.runnersutil.domain.entities.Run;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/status")
    public String testEndpoint() {
        return "ok";
    }

    @GetMapping("/test-runs")
    public Set<RunDTO> getRuns() {
        Set<RunDTO> runDTOs = new HashSet<>();
        RunDTO run1 = new RunDTO();
        run1.setRunId("1");
        run1.setStartDateTime(new Date());
        run1.setDuration(3300000L);
        run1.setDistance(5.0);
        run1.setUserId("user_1");
        run1.setRouteId("route_1");

        RunDTO run2 = new RunDTO();
        run2.setRunId("2");
        run2.setStartDateTime(new Date());
        run2.setDuration(3800000L);
        run2.setDistance(7.0);
        run2.setUserId("user_2");
        run2.setRouteId("route_2");

        RunDTO run3 = new RunDTO();
        run3.setRunId("3");
        run3.setStartDateTime(new Date());
        run3.setDuration(3400000L);
        run3.setDistance(10.0);
        run3.setUserId("user_3");
        run3.setRouteId("route_3");

        runDTOs.addAll(Arrays.asList(run1, run2, run3));

        return runDTOs;
    }
}
