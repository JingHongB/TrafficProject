package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 */
@Configuration
public class WebConfiguration {
    /**
     * 配置 RestTemplate Bean，用于发起 HTTP 请求。
     *
     * @return RestTemplate 实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 配置跨域请求，允许前端应用从特定来源访问后端资源。
     *
     * @return WebMvcConfigurer 实例
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // 允许来自 http://localhost:5173 的请求
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                        .allowedHeaders("*") // 允许所有请求头
                        .allowCredentials(true) // 允许发送凭据
                        .maxAge(3600); // 设置预检请求的缓存时间为 3600 秒
            }
        };
    }
}
