package com.movie.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request payload for creating or updating a film director")
public class DirectorRequestDTO {

    @NotBlank
    @Schema(description = "Full name of the director", example = "Aditya Dhar")
    private String name;

    @Schema(description = "Nationality of the director", example = "Indian")
    private String nationality;

    @Schema(description = "Year of birth", example = "1983")
    private Integer birthYear;
}