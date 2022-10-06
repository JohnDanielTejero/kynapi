package com.kyn.api.kynapi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyn.api.kynapi.Model.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long>{
    
    public Optional<Roles> findByRoleName(String roleName);
}
