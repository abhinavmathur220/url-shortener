package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UrlService {

    @Autowired
    private UrlMappingRepository repository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String shortenUrl(String longUrl) {
        String shortCode = UUID.randomUUID().toString().substring(0, 8);
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setLongUrl(longUrl);
        repository.save(mapping);
        redisTemplate.opsForValue().set(shortCode, longUrl, 1, TimeUnit.DAYS);
        return shortCode;
    }

    public Optional<String> getLongUrl(String shortCode) {
        String cached = redisTemplate.opsForValue().get(shortCode);
        if (cached != null) return Optional.of(cached);

        Optional<UrlMapping> result = repository.findByShortCode(shortCode);
        result.ifPresent(mapping -> redisTemplate.opsForValue()
                .set(shortCode, mapping.getLongUrl(), 1, TimeUnit.DAYS));
        return result.map(UrlMapping::getLongUrl);
    }
}
