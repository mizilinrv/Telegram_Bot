package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.UniqueUser;
import com.mizilin.firstbot.entity.User;
import com.mizilin.firstbot.repository.UniqueUserRepository;
import com.mizilin.firstbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.security.core.userdetails.User.withUsername;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UniqueUserRepository uniqueUserRepository;
    @Transactional
    public void saveUser(final long id) {
        uniqueUserRepository.save(new UniqueUser(id));
    }

    public String userCount() {
        return String.valueOf(uniqueUserRepository.count());
    }

    public List<UniqueUser> allUsers() {
        return uniqueUserRepository.findAll();
    }

    public User findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = findByUsername(username);
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


