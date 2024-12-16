package com.dkb.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlResolveResponse {

  private String originalUrl;
}
