package com.example.userservice.service;

import com.example.userservice.entities.ApiUser;

public interface BasicUserDetailsService {
    public ApiUser getUserByUsername(String username);
}
