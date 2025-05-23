package com.task.manager.gatewayservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.task.manager.gatewayservice.entity.*;
import com.task.manager.gatewayservice.repository.TokenvalidRepository;
import com.task.manager.gatewayservice.security.JwtUtil;
import com.task.manager.gatewayservice.security.RateLimiterService;

import io.github.bucket4j.Bucket;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private RateLimiterService rateLimiterService;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private TokenvalidRepository tokenvalidRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody SimpleUserDTO ruser, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimiterService.resolveBucket(ip,"login");
        if (bucket.tryConsume(1)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SimpleUserDTO> entity = new HttpEntity<>(ruser,headers);

            String uri = "http://localhost:8081/auth/login";
             
            try {
                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                // This captures 4xx and 5xx errors and returns them as-is
                return ResponseEntity
                        .status(ex.getStatusCode())
                        .body(ex.getResponseBodyAsString());

            } catch (Exception ex) {
                // Fallback for unexpected issues (e.g., timeout, no connection)
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An unexpected error occurred: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many attempts");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequestDTO request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OtpVerificationRequestDTO> entity = new HttpEntity<>(request,headers);

        String uri = "http://localhost:8081/auth/verify-otp";
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            String token=response.getBody();
            Tokenvalid newU=new Tokenvalid(jwtUtils.getUserIdFromToken(token), LocalDateTime.now());
            tokenvalidRepository.save(newU);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDTO> entity = new HttpEntity<>(user,headers);

        String uri = "http://localhost:8081/auth/register";

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.POST, entity, String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
        
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDTO user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDTO> entity = new HttpEntity<>(user,headers);

        String uri = "http://localhost:8081/auth/register/admin";
         
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }
}

