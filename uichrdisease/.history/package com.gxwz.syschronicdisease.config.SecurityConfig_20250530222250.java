package com.gxwz.syschronicdisease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests()
                .antMatchers("/user/**","/division/**","/insureds/**","/medicalCard/**","/disease/**","/policy/**","/reimbursement/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置允许的源，使用具体的域名
        configuration.setAllowedOrigins(Arrays.asList("http://154.12.36.159"));
        // 设置允许的方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // 设置允许的头
        configuration.setAllowedHeaders(Arrays.asList(
            "Content-Type", 
            "X-CAF-Auth-Token", 
            "sessionToken", 
            "token", 
            "customercoderoute", 
            "authorization", 
            "conntectionid", 
            "Cookie", 
            "request-ajax",
            "X-Requested-With"
        ));
        // 设置允许的凭证
        configuration.setAllowCredentials(true);
        // 设置暴露的头
        configuration.setExposedHeaders(Arrays.asList("Content-Length", "Content-Range"));
        // 设置预检请求的有效期
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 