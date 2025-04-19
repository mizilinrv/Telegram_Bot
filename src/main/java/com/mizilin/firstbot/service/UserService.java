package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.UniqueUser;
import com.mizilin.firstbot.entity.User;
import com.mizilin.firstbot.repository.UniqueUserRepository;
import com.mizilin.firstbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.security.core.userdetails.User.withUsername;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UniqueUserRepository uniqueUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, UniqueUserRepository uniqueUserRepository) {
        this.userRepository = userRepository;
        this.uniqueUserRepository = uniqueUserRepository;
    }

    @Transactional
    public void saveUser(long chatId) {
        uniqueUserRepository.save(new UniqueUser(chatId));
    }

    public String userCount() {
        return String.valueOf(uniqueUserRepository.count());
    }

    public List<UniqueUser> allUsers() {
        return uniqueUserRepository.findAll();
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
