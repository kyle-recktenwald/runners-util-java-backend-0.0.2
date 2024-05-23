package com.krecktenwald.runnersutil.domain.dto.mapper.impl.run;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.CrudEntityInfoDto;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.route.RouteDto;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunDto {
  private String runId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_date_time")
  private Date startDateTime;

  private Long duration;

  private Double distance;

  private String userId;

  private RouteDto route;

  private CrudEntityInfoDto crudEntityInfo;
}
