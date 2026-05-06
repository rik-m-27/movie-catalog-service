package com.movie.catalog.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Response payload containing full movie details")
public class MovieResponseDTO {

    @Schema(example = "101")
    private Long id;

    @Schema(example = "URI: The Surgical Strike")
    private String title;

    @Schema(example = "2019")
    private Integer releaseYear;

    @Schema(example = "8.2")
    private Double rating;

    private DirectorResponseDTO director;

    private Set<GenreResponseDTO> genres;
}