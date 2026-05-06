package com.movie.catalog.dto;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Response payload containing director details")
public class DirectorResponseDTO {

    @Schema(description = "Unique director ID", example = "1")
    private Long id;

    @Schema(example = "Aditya Dhar")
    private String name;

    @Schema(example = "Indian")
    private String nationality;

    @Schema(example = "1983")
    private Integer birthYear;
}