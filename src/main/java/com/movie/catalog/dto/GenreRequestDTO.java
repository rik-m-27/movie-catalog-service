package com.movie.catalog.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request payload for creating a movie genre")
public class GenreRequestDTO {

    @NotBlank
    @Schema(
        description = "Name of the genre",
        example = "Action"
    )
    private String name;
}