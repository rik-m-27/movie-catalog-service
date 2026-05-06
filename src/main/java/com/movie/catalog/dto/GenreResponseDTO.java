package com.movie.catalog.dto;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Response payload containing genre details")
public class GenreResponseDTO {

    @Schema(description = "Unique genre ID", example = "1")
    private Long id;

    @Schema(description = "Name of the genre", example = "Action")
    private String name;
}