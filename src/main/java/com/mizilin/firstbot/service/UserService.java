package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.User;
import com.mizilin.firstbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import static org.springframework.security.core.userdetails.User.withUsername;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
        }
        return withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
