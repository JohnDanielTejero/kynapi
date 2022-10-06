package com.kyn.api.kynapi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyn.api.kynapi.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    public User findByUserId(long id);
    public User findByEmail(String email);
}
