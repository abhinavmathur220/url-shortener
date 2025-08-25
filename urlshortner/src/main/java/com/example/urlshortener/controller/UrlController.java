package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UrlController 
{
    @Autowired
    private UrlService urlService;
    
    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> request) 
    {
        String longUrl = request.get("url");
        String shortCode = urlService.shortenUrl(longUrl);
        return ResponseEntity.ok(Map.of("shortCode", shortCode));
    }
    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        return urlService.getLongUrl(shortCode)
                .map(url -> ResponseEntity.status(302)
                        .header("Location", url)
                        .build())
                .orElse(ResponseEntity.notFound().build());
    }
}