package com.task.manager.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.manager.userservice.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}