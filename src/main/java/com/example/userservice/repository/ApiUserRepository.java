package com.example.userservice.repository;

import com.example.userservice.entities.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUserRepository extends JpaRepository<ApiUser, Integer> {
    ApiUser findApiUsersByUsername(String username);
}
