package com.task.manager.gatewayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.manager.gatewayservice.entity.Tokenvalid;

public interface  TokenvalidRepository extends JpaRepository<Tokenvalid, Long> {
    
}
