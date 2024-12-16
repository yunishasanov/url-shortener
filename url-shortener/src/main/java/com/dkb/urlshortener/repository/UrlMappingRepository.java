package com.dkb.urlshortener.repository;

import com.dkb.urlshortener.entity.UrlMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

  Optional<UrlMapping> findByHash(String shortUrl);
}
