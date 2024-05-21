package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "routes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Route {
  @Id
  @GenericGenerator(name = "route_id", strategy = "uuid2")
  private String routeId;

  @Column(name = "name")
  private String name;

  @Column(name = "distance")
  private Double distance;

  private String userId;

  @OneToMany(mappedBy = "route")
  private Set<Run> runs;

  @Embedded private CrudEntityInfo crudEntityInfo;
}
