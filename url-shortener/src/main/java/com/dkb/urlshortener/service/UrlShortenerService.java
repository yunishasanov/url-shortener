package com.dkb.urlshortener.service;

import com.dkb.urlshortener.entity.UrlMapping;
import com.dkb.urlshortener.exception.UrlNotFoundException;
import com.dkb.urlshortener.repository.UrlMappingRepository;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

  private final UrlMappingRepository urlMappingRepository;
  private final int hashSeed;

  public UrlShortenerService(
      UrlMappingRepository urlMappingRepository,
      @Value("${hash.seed}") int hashSeed) {
    this.urlMappingRepository = urlMappingRepository;
    this.hashSeed = hashSeed;
  }

  public String shortenUrl(String originalUrl) {
    validateUrl(originalUrl);

    String hash = computeHash(originalUrl);

    Optional<UrlMapping> existingMapping = urlMappingRepository.findByHash(hash);
    if (existingMapping.isPresent()) {
      if (!existingMapping.get().getOriginalUrl().equals(originalUrl)) {
        throw new IllegalStateException("Hash collision detected for URL: " + originalUrl);
      }
      return existingMapping.get().getHash();
    }

    UrlMapping mapping = new UrlMapping(originalUrl, hash, ZonedDateTime.now(ZoneOffset.UTC));
    urlMappingRepository.save(mapping);
    return hash;
  }

  public String resolveUrl(String hash) {
    UrlMapping mapping = urlMappingRepository.findByHash(hash)
        .orElseThrow(() -> new UrlNotFoundException("URL not found for hash: " + hash));
    return mapping.getOriginalUrl();
  }

  private void validateUrl(String url) {
    try {
      new java.net.URL(url);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid URL: " + url, e);
    }
  }

  private String computeHash(String url) {
    return Hashing.murmur3_32_fixed(hashSeed)
        .hashString(url, StandardCharsets.UTF_8)
        .toString();
  }
}