package com.example.security.auth.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            createErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing", null);
            return;
        }
        if (!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            createErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is invalid", null);
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    createErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token", null);
                }
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            createErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token", e);
        } catch (Exception e) {
            createErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    private void createErrorResponse(HttpServletResponse response, int status, String message, Exception e) throws IOException {
        String logMessage = message;
        if (e != null) {
            logMessage += ": " + e.getClass().getSimpleName() + ": " + e.getMessage();
        }
        log.warn("Authorization exception with status {}: {}", status, logMessage);
        response.setStatus(status);
        response.setContentType("application/json");
        var errorResponse = Map.of("error", message);
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
