package com.vgr.account_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {
	
	 @Value("${jwt.secret}")
	 private String SECRET_KEY;

    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = SECRET_KEY;
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}
