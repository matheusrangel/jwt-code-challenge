package com.matheusrangel.jwtcodechallenge.controller;

import com.matheusrangel.jwtcodechallenge.dto.response.AuthResponse;
import com.matheusrangel.jwtcodechallenge.service.AuthService;
import com.matheusrangel.jwtcodechallenge.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Valida token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token JWT válido",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) } ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida") })
    public ResponseEntity<AuthResponse> authenticate(
            @Parameter(description = "Token JWT", required = true)
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
