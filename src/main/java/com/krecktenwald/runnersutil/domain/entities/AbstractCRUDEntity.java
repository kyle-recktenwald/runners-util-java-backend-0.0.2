package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractCRUDEntity {
  @Enumerated(EnumType.STRING)
  private CrudStatus crudStatus;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date")
  private Date updateDate;

  @ManyToOne private User createdBy;

  @ManyToOne private User updatedBy;
}
