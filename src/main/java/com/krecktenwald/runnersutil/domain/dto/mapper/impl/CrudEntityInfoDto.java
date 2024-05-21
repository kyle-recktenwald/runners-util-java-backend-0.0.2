package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class CrudEntityInfoDto {
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private Date createDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private Date updateDate;

  private String createdByUserId;

  private String updatedByUserId;

  private Boolean isDeleted;
}
