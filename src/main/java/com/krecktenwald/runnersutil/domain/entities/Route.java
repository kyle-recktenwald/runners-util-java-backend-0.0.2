package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
public class Route extends AbstractCRUDEntity {
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
}
