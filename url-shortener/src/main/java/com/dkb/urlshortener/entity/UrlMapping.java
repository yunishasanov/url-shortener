package com.dkb.urlshortener.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
public class UrlMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String originalUrl;
  private String hash;
  private ZonedDateTime createdAt;


  public UrlMapping(String originalUrl, String hash, ZonedDateTime createdAt) {
    this.originalUrl = originalUrl;
    this.hash = hash;
    this.createdAt = createdAt;
  }

}
