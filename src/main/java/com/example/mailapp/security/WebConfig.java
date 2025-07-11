package com.example.mailapp.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(
                                "http://localhost:3003",
                                "https://mail-app-react-nxbmngtba-hemachandirans-projects.vercel.app"

                        )

                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Include OPTIONS

                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
