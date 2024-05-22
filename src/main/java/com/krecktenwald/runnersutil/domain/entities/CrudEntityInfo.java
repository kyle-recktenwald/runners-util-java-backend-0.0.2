package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CrudEntityInfo {
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false)
  private final Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date")
  private Date updateDate;

  @Column(name = "created_by", nullable = false)
  private final String createdBy;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

  public CrudEntityInfo(String createdBy) {
    if (createdBy == null || createdBy.trim().isEmpty()) {
      throw new IllegalArgumentException("createdBy cannot be null or empty");
    }
    this.createDate = new Date();
    this.createdBy = createdBy;
    this.updateDate = null;
    this.updatedBy = null;
    this.isDeleted = false;
  }
}
