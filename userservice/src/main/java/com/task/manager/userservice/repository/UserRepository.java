package com.task.manager.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.manager.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByAcvalidatedFalse();

}