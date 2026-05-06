package com.movie.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AppInfoResponseDTO(
    @Schema(description = "The official name of the microservice", example = "inventory-service")
    String name,

    @Schema(description = "The current semantic version of the application", example = "1.2.4-RELEASE")
    String version
) {}
