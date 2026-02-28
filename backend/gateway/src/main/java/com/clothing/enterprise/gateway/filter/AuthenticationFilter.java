package com.clothing.enterprise.gateway.filter;

import com.clothing.enterprise.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Check if Authorization header exists
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7); // Remove "Bearer "
            } else {
                return onError(exchange, "Invalid Authorization Header Format", HttpStatus.UNAUTHORIZED);
            }

            try {
                // 2. Validate the Token
                jwtUtil.validateToken(authHeader);

                // 3. Extract Identity and Mutate the Request
                String userId = jwtUtil.extractUserId(authHeader);

                // This is the magic: We securely inject the X-User-Id header
                request = exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", userId)
                        .build();

            } catch (Exception e) {
                return onError(exchange, "Unauthorized access to application", HttpStatus.UNAUTHORIZED);
            }

            // 4. Forward the mutated request to the downstream service
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    private Mono<Void> onError(org.springframework.web.server.ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        // Configuration properties can go here if needed
    }
}