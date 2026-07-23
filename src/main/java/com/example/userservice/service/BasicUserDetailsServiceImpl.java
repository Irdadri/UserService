package com.example.userservice.service;

import com.example.userservice.entities.ApiUser;
import com.example.userservice.repository.ApiUserRepository;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("basicUserDetailsService")
@Log
public class BasicUserDetailsServiceImpl implements BasicUserDetailsService, UserDetailsService {
    public final ApiUserRepository apiUserRepository;

    public BasicUserDetailsServiceImpl(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser user = getUserByUsername(username);

        log.info(user.getUsername() + "---" + user.getPassword());

        if(user == null){
            throw new UsernameNotFoundException("errore");
        }

        return new BasicUserDetails(user);
    }

    @Override
    public ApiUser getUserByUsername(String username) {
        return apiUserRepository.findApiUsersByUsername(username);
    }
}
