package com.example.urlshortener.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties
{
    /** Max requests allowed in the window. */
    private int limit = 10;

    /** Window size in seconds. */
    private int windowSeconds = 60;

    /** Whether to enable the interceptor globally. */
    private boolean enabled = true;

    /** Apply only to write endpoints by default (e.g., /v1/shorten). */
    private String[] includePatterns = new String[] {"/v1/shorten"};

    public int getLimit()
    {
        return limit;
    }
    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getWindowSeconds()
    {
        return windowSeconds;
    }
    public void setWindowSeconds(int windowSeconds)
    {
        this.windowSeconds = windowSeconds;
    }
    public boolean isEnabled()
    {
        return enabled;
    }
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
    public String[] getIncludePatterns()
    {
        return includePatterns;
    }
    public void setIncludePatterns(String[] includePatterns)
    {
        this.includePatterns = includePatterns;
    }

    public String getExcludePatterns()
    {
        return "";
    }
}