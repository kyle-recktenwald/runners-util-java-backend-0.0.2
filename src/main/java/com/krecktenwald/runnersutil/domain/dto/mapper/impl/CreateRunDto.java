package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateRunDto {
  @NotNull
  private String userId;

  @NotNull
  private String createdByUserId;

  private String routeId;

  @NotNull
  private Double distance;

  @NotNull
  private Long duration;

  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDateTime;
}
