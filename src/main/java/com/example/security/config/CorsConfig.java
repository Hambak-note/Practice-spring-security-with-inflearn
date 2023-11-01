package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter; //스프링에서 제공하는 CorsFilter

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 내버서가 응답할 때 json 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.setAllowCredentials(true);
        //모든 ip에 응답을 허용하겠다.
        config.addAllowedOrigin("*");
        //모든 헤더의 응답을 허용하겠다.
        config.addAllowedHeader("*");
        //모든 post.get.put.delete.patch를 허용하겠다.
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
