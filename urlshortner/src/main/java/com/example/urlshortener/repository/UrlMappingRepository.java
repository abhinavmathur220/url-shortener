package com.example.urlshortener.repository;

import com.example.urlshortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> 
{
    Optional<UrlMapping> findByShortCode(String shortCode);
}