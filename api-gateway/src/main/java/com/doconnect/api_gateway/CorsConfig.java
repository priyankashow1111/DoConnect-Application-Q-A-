package com.doconnect.api_gateway;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//
//        // Allow requests from your frontend
//        corsConfig.setAllowedOrigins(Arrays.asList("*"));
//
//        // Allow these HTTP methods
//        corsConfig.setAllowedMethods(Arrays.asList(
//            "GET", "POST", "PUT", "DELETE", "OPTIONS"
//        ));
//
//        // Allow these headers
//        corsConfig.setAllowedHeaders(Arrays.asList(
//            "Authorization",
//            "Content-Type",
//            "X-Requested-With"
//        ));
//
//        UrlBasedCorsConfigurationSource source =
//            new UrlBasedCorsConfigurationSource();
//
//        // Apply to all routes
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
//}


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Allow ALL origins
        corsConfig.setAllowedOriginPatterns(List.of("*"));

        // Allow ALL methods
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE",
            "OPTIONS", "PATCH", "HEAD"
        ));

        // Allow ALL headers
        corsConfig.setAllowedHeaders(List.of("*"));

        // Allow credentials
        corsConfig.setAllowCredentials(false);

        // Cache preflight for 1 hour
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}