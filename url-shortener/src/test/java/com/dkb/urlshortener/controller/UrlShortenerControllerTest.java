package com.dkb.urlshortener.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dkb.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UrlShortenerService urlShortenerService;

  @Test
  void shortenUrl_ShouldReturnShortenedUrl_WhenRequestIsValid() throws Exception {
    String originalUrl = "https://example.com";
    String shortenedUrl = "e9da59dd";
    Mockito.when(urlShortenerService.shortenUrl(eq(originalUrl))).thenReturn(shortenedUrl);

    String requestBody = """
        {
          "url": "%s"
        }
        """.formatted(originalUrl);

    mockMvc.perform(post("/api/v1/urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.hash").value(shortenedUrl));
  }

  @Test
  void shortenUrl_ShouldThrowException_WhenRequestUrlIsEmpty() throws Exception {
    String requestBody = """
        {
          "url": ""
        }
        """;

    mockMvc.perform(post("/api/v1/urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(
            status().isInternalServerError()) // Matches IllegalStateException in ControllerAdvice
        .andExpect(jsonPath("$.details").value("Url could not be empty or null."));
  }

  @Test
  void resolveUrl_ShouldReturnOriginalUrl_WhenHashExists() throws Exception {
    String hash = "e9da59dd";
    String originalUrl = "https://example.com";
    Mockito.when(urlShortenerService.resolveUrl(eq(hash))).thenReturn(originalUrl);

    mockMvc.perform(get("/api/v1/urls/" + hash))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalUrl").value(originalUrl));
  }

  @Test
  void resolveUrl_ShouldThrowException_WhenHashDoesNotExist() throws Exception {
    String hash = "invalidHash";
    Mockito.when(urlShortenerService.resolveUrl(eq(hash)))
        .thenThrow(new IllegalArgumentException("URL not found"));

    mockMvc.perform(get("/api/v1/urls/" + hash))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("URL not found"));
  }
}