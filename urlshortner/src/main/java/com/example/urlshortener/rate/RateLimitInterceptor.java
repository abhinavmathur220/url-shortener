package com.example.urlshortener.rate;

import com.example.urlshortener.config.RateLimitProperties;
import com.example.urlshortener.exception.RateLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor
{

    public final RateLimiterService limiter;
    public final RateLimitProperties props;

    public RateLimitInterceptor(RateLimiterService limiter, RateLimitProperties props) {
        this.limiter = limiter;
        this.props = props;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!props.isEnabled()) {
            return true; // Rate limiting disabled
        }

        // Identify the caller (use userId if auth is added later)
        String ip = clientIp(request);
        String path = request.getRequestURI();
        String key = ip + "|" + path;

        long remaining = limiter.consume(key);

        // Set helpful headers for client
        response.setHeader("X-RateLimit-Limit", String.valueOf(props.getLimit()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, remaining)));
        response.setHeader("X-RateLimit-Window-Seconds", String.valueOf(props.getWindowSeconds()));

        // If over the limit → block
        if (remaining < 0) {
            throw new RateLimitExceededException("Rate limit exceeded. Try again later.");
        }

        return true;
    }

    private String clientIp(HttpServletRequest request)
    {
        // Respect reverse proxy headers if present
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String xrip = request.getHeader("X-Real-IP");
        if (xrip != null && !xrip.isBlank()) {
            return xrip;
        }
        return request.getRemoteAddr();
    }
}
