package com.task.manager.gatewayservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.task.manager.gatewayservice.repository.TokenvalidRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired 
    private TokenvalidRepository tokenvalidRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("Request: {} {} from {}",request.getMethod(),request.getRequestURI(),request.getRemoteAddr());

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        List<SimpleGrantedAuthority> role =null;
        LocalDateTime lastPassReset=null;
        long user_id=0;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtils.getUsernameFromToken(token);
            role= Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + jwtUtils.getRoleFromToken(token)));
            user_id = jwtUtils.getUserIdFromToken(token);
            lastPassReset = tokenvalidRepository.findById(user_id).get().getLast_password_reset();
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //var userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateJwtToken(token, lastPassReset)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, role);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        
        filterChain.doFilter(request, response);

        logger.info("Response: {} (Status: {})",request.getRequestURI(),response.getStatus()
        );
    }
}