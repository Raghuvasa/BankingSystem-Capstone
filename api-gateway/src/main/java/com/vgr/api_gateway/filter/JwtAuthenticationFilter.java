package com.vgr.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.vgr.api_gateway.validate.RouteValidator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.core.HttpHeaders;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private RouteValidator routeValidator;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
    }
    private java.security.Key getSigningKey() {
    	byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    	return  Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

        	log.info("gateway started actual flow ...{}");
            String path = exchange.getRequest().getURI().getPath();
            log.info("Incoming request for path: {}", path);

            if (routeValidator.isSecured.test(exchange.getRequest())) {

                log.info("Secured route detected: {}", path);

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    log.warn("Authorization header is missing for path: {}", path);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }


                String token = authHeader.substring(7).trim();
                log.debug("JWT token received for validation");

                try {

                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String username = claims.getSubject();
                    log.info("JWT validated successfully. Authenticated user: {}", username);

                    exchange = exchange.mutate()
                            .request(builder -> builder.header("X-Authenticated-User", username))
                            .build();

                } catch (Exception e) {

                    log.error("JWT validation failed for path: {}. Error: {}", path, e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } else {
                log.info("Public route accessed: {}", path);
            }

            log.info("Passing request to next filter for path: {}", path);
            return chain.filter(exchange);
        };
    }
}