package com.movie.catalog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreResponseDTO {
    private Long id;
    private String name;
}