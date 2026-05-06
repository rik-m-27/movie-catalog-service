package com.movie.catalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "catalog")
public record CatalogConfig(
    Integer maxGenresPerMovie, 
    Double defaultRating
) {
}
