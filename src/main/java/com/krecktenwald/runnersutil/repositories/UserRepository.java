package com.krecktenwald.runnersutil.repositories;

import com.krecktenwald.runnersutil.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}
