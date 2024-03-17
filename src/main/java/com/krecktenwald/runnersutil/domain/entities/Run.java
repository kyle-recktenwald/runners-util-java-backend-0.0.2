package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "runs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Run extends AbstractCRUDEntity {
  @Id
  @GenericGenerator(name = "run_id", strategy = "uuid2")
  private String runId;

  @Column(name = "distance")
  private Double distance;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_date_time")
  private Date startDateTime;

  @Column(name = "duration")
  private Long duration;

  @ManyToOne
  @JoinColumn(name = "route_id")
  private Route route;

  private String userId;
}
