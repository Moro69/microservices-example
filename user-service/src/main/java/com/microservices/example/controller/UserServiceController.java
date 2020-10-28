package com.microservices.example.controller;

import com.microservices.example.domain.dto.AddUserRequest;
import com.microservices.example.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserServiceController {

    private final UserService userService;

    @Autowired
    public UserServiceController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody @Valid AddUserRequest addUserRequest) {
        log.debug("addUser: email = {}, username = {}", addUserRequest.getEmail(), addUserRequest.getUsername());

        userService.addUser(addUserRequest);

        return ResponseEntity.ok().build();
    }
}
