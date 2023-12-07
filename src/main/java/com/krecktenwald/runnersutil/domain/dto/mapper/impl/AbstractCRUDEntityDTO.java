package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import com.krecktenwald.runnersutil.domain.entities.CrudStatus;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public abstract class AbstractCRUDEntityDTO {
  private CrudStatus crudStatus;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private Date createDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private Date updateDate;

  private String createdBy;

  private String updatedBy;
}
