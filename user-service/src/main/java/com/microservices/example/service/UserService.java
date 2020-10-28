package com.microservices.example.service;

import com.microservices.example.domain.auth.UserRole;
import com.microservices.example.domain.dto.AddUserRequest;
import com.microservices.example.domain.entity.User;
import com.microservices.example.domain.error.ErrorCodes;
import com.microservices.example.domain.error.RestException;
import com.microservices.example.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(AddUserRequest addUserRequest) {
        log.debug("addUser: email = {}, username = {}", addUserRequest.getEmail(), addUserRequest.getUsername());

        checkIfEmailIsTaken(addUserRequest.getEmail());
        checkIfUsernameIsTaken(addUserRequest.getUsername());

        userRepository.save(User.builder()
                .email(addUserRequest.getEmail())
                .username(addUserRequest.getUsername())
                .password(passwordEncoder.encode(addUserRequest.getPassword()))
                .userRole(UserRole.ROLE_USER)
                .build());
    }

    private void checkIfEmailIsTaken(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RestException(ErrorCodes.EMAIL_IS_TAKEN);
        }
    }

    private void checkIfUsernameIsTaken(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new RestException(ErrorCodes.USERNAME_IS_TAKEN);
        }
    }
}
