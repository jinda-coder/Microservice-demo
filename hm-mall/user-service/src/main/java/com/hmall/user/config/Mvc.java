package com.hmall.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Mvc implements WebMvcConfigurer {
    @Autowired
    MvcInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
