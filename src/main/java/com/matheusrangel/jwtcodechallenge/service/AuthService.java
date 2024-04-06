package com.matheusrangel.jwtcodechallenge.service;

public interface AuthService {

    Boolean authenticate(String token);
}
