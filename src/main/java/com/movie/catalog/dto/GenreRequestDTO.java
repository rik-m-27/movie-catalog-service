package com.movie.catalog.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreRequestDTO {
    @NotBlank
    private String name;
}