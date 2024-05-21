package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractCRUDEntity {
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date")
  private Date updateDate;

  private String createdByUserId;

  private String updatedByUserId;

  private Boolean isDeleted;
}
