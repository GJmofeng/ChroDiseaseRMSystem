package com.gxwz.syschronicdisease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许跨域的源地址
        config.addAllowedOrigin("http://localhost:5173"); // Vite开发服务器默认端口
        
        // 允许跨域的请求头
        config.addAllowedHeader("*");
        
        // 允许跨域的请求方法
        config.addAllowedMethod("*");
        
        // 允许携带cookie等认证信息
        config.setAllowCredentials(true);
        
        // 对所有接口都有效
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
} 