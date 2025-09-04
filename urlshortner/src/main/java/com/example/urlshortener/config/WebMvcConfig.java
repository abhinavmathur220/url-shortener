package com.example.urlshortener.config;

import com.example.urlshortener.rate.RateLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;
    private final RateLimitProperties props;

    public WebMvcConfig(RateLimitInterceptor rateLimitInterceptor, RateLimitProperties props) {
        this.rateLimitInterceptor = rateLimitInterceptor;
        this.props = props;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (props.isEnabled()) {
            registry.addInterceptor(rateLimitInterceptor)
                    .addPathPatterns(props.getIncludePatterns())
                    .excludePathPatterns(props.getExcludePatterns()); // optional
        }
    }
}
