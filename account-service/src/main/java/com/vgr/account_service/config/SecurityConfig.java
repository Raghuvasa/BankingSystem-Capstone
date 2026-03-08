package com.vgr.account_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/accounts/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
								.permitAll().anyRequest().authenticated())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
				//.oauth2ResourceServer(oauth -> oauth.jwt());
		return http.build();
	}
}