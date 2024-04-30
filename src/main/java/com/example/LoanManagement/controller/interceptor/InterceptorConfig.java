package com.example.LoanManagement.controller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/user/**");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");
    }
}
