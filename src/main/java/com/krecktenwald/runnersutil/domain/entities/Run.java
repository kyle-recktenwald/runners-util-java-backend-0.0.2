package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.*;
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
public class Run {
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

  @Embedded private CrudEntityInfo crudEntityInfo;
}
