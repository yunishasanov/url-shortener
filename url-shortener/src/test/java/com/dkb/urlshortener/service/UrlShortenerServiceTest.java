package com.dkb.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dkb.urlshortener.entity.UrlMapping;
import com.dkb.urlshortener.exception.UrlNotFoundException;
import com.dkb.urlshortener.repository.UrlMappingRepository;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UrlShortenerServiceTest {

  private final int hashSeed = 123456;
  @Mock
  private UrlMappingRepository urlMappingRepository;
  private UrlShortenerService urlShortenerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    urlShortenerService = new UrlShortenerService(urlMappingRepository, hashSeed);
  }

  @Test
  void shortenUrl_ShouldReturnHash_WhenUrlIsNew() {
    String originalUrl = "http://example.com";
    String expectedHash = "e9da59dd";

    when(urlMappingRepository.findByHash(expectedHash)).thenReturn(Optional.empty());
    when(urlMappingRepository.save(any(UrlMapping.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    String result = urlShortenerService.shortenUrl(originalUrl);

    assertEquals(expectedHash, result);
    verify(urlMappingRepository).save(any(UrlMapping.class));
  }

  @Test
  void shortenUrl_ShouldReturnExistingHash_WhenUrlAlreadyExists() {
    String originalUrl = "http://example.com";
    String existingHash = "e9da59dd";
    UrlMapping existingMapping = new UrlMapping(originalUrl, existingHash, ZonedDateTime.now());

    when(urlMappingRepository.findByHash(existingHash)).thenReturn(Optional.of(existingMapping));

    String result = urlShortenerService.shortenUrl(originalUrl);

    assertEquals(existingHash, result);
    verify(urlMappingRepository, never()).save(any(UrlMapping.class));
  }

  @Test
  void shortenUrl_ShouldThrowException_WhenHashCollisionOccurs() {
    String originalUrl = "http://example.com";
    String collidingUrl = "http://different.com";
    String hash = "e9da59dd";

    UrlMapping existingMapping = new UrlMapping(collidingUrl, hash, ZonedDateTime.now());

    when(urlMappingRepository.findByHash(hash)).thenReturn(Optional.of(existingMapping));

    IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
        urlShortenerService.shortenUrl(originalUrl));

    assertTrue(exception.getMessage().contains("Hash collision detected"));
  }

  @Test
  void resolveUrl_ShouldReturnOriginalUrl_WhenHashExists() {
    String hash = "5cadbe1c";
    String originalUrl = "http://example.com";
    UrlMapping mapping = new UrlMapping(originalUrl, hash, ZonedDateTime.now());

    when(urlMappingRepository.findByHash(hash)).thenReturn(Optional.of(mapping));

    String result = urlShortenerService.resolveUrl(hash);

    assertEquals(originalUrl, result);
  }

  @Test
  void resolveUrl_ShouldThrowUrlNotFoundException_WhenHashDoesNotExist() {
    String hash = "nonexistent";

    when(urlMappingRepository.findByHash(hash)).thenReturn(Optional.empty());

    UrlNotFoundException exception = assertThrows(UrlNotFoundException.class, () ->
        urlShortenerService.resolveUrl(hash));

    assertTrue(exception.getMessage().contains("URL not found for hash"));
  }

  @Test
  void shortenUrl_ShouldThrowIllegalArgumentException_WhenUrlIsInvalid() {
    String invalidUrl = "invalid-url";

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        urlShortenerService.shortenUrl(invalidUrl));

    assertTrue(exception.getMessage().contains("Invalid URL"));
  }
}