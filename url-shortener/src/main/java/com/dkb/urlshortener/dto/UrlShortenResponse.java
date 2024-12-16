package com.dkb.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UrlShortenResponse {
  private String hash;
}
