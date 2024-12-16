package com.dkb.urlshortener.controller;

import com.dkb.urlshortener.dto.UrlResolveResponse;
import com.dkb.urlshortener.dto.UrlShortenRequest;
import com.dkb.urlshortener.dto.UrlShortenResponse;
import com.dkb.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

  private final UrlShortenerService urlShortenerService;

  @PostMapping
  public ResponseEntity<UrlShortenResponse> shortenUrl(@RequestBody UrlShortenRequest request) {
    if (!StringUtils.hasLength(request.getUrl())) {
      throw new IllegalStateException("Url could not be empty or null.");
    }
    String shortenedUrl = urlShortenerService.shortenUrl(request.getUrl());
    return ResponseEntity.ok(new UrlShortenResponse(shortenedUrl));
  }

  @GetMapping("/{hash}")
  public ResponseEntity<UrlResolveResponse> resolveUrl(@PathVariable String hash) {
    String originalUrl = urlShortenerService.resolveUrl(hash);
    return ResponseEntity.ok(new UrlResolveResponse(originalUrl));
  }
}
