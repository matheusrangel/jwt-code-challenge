package com.matheusrangel.jwtcodechallenge.controller;

import com.matheusrangel.jwtcodechallenge.service.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Boolean> authenticate(
            @RequestHeader("Authorization") String token
    ) {
        var valid = authService.authenticate(token);

        if (valid) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(false);
    }
}