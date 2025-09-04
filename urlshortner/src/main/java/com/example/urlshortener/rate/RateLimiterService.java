package com.example.urlshortener.rate;

import com.example.urlshortener.config.RateLimitProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService
{

    private final StringRedisTemplate redis;
    private final RateLimitProperties props;

    public RateLimiterService(StringRedisTemplate redis, RateLimitProperties props)
    {
        this.redis = redis;
        this.props = props;
    }

    /**
     * @param key unique key per caller and route, e.g., ip+path or userId+path
     * @return remaining quota after this call; -1 if limiter disabled
     */
    public long consume(String key)
    {
        if (!props.isEnabled()) return -1; // disabled

        String redisKey = buildKey(key, props.getWindowSeconds());
        Long count = redis.opsForValue().increment(redisKey);
        if (count != null && count == 1L)
        {
        // first hit in this window → set TTL
            redis.expire(redisKey, Duration.ofSeconds(props.getWindowSeconds()));
        }
        return props.getLimit() - (count == null ? 0 : count);
    }

    private String buildKey(String rawKey, int windowSec)
    {
        long windowStart = System.currentTimeMillis() / 1000 / windowSec; // coarse bucket
        return "rl:" + rawKey + ":w:" + windowStart;
    }
}