package com.krecktenwald.runnersutil.domain.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Role extends AbstractCRUDEntity {

  @Id
  @GenericGenerator(name = "role_id", strategy = "uuid2")
  private String roleId;

  private String name;
  private String description;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users;

  @ElementCollection(targetClass = Permission.class)
  @CollectionTable(name = "role_permissions")
  @Enumerated(EnumType.STRING)
  @Column(name = "permission")
  private Set<Permission> permissions = new HashSet<>();
}
