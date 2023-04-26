package com.wcs.project3.repository;

import com.wcs.project3.entity.ERole;
import com.wcs.project3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
