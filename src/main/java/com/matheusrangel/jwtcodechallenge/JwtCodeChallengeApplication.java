package com.matheusrangel.jwtcodechallenge;

import com.matheusrangel.jwtcodechallenge.config.JWTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
		JWTProperties.class
})
@SpringBootApplication
public class JwtCodeChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtCodeChallengeApplication.class, args);
	}

}
