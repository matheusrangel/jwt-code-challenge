package com.matheusrangel.jwtcodechallenge.controller;

import com.matheusrangel.jwtcodechallenge.dto.response.AuthResponse;
import com.matheusrangel.jwtcodechallenge.service.AuthService;
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

    private final AuthService authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<AuthResponse> authenticate(
            @RequestHeader("Authorization") String token
    ) {
        var valid = authService.authenticate(token);

        if (valid) {
            return ResponseEntity.ok(new AuthResponse(true));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(false));
    }
}
