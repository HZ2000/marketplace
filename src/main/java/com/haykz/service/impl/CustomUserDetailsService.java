package com.haykz.service.impl;

import com.haykz.entity.UserEntity;
import com.haykz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Custom implementation of {@link UserDetailsService} for loading user details from the database.
 * <p>
 * This service retrieves user information based on the provided username and creates a Spring Security {@link UserDetails}
 * object to be used for authentication and authorization purposes. If no user is found with the given username,
 * a {@link UsernameNotFoundException} is thrown.
 * </p>
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                new ArrayList<>()
        );
    }
}
