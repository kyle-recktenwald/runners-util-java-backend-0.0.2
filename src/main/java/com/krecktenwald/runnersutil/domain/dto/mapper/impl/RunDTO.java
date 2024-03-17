package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunDTO extends AbstractCRUDEntityDTO {
  private String runId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_date_time")
  private Date startDateTime;

  private Long duration;

  private Double distance;

  private String userId;

  private String routeId;
}
