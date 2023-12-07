package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User extends AbstractCRUDEntity {

  @Id
  @GenericGenerator(name = "user_id", strategy = "uuid2")
  private String userId;

  private String authId;

  private String emailAddress;

  @OneToMany(mappedBy = "runOwner")
  private Set<Run> runs;

  @OneToMany(mappedBy = "routeOwner", cascade = CascadeType.ALL)
  private Set<Route> routes = new HashSet<>();

  @ManyToMany private Set<Role> roles;
}
