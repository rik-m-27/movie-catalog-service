package com.movie.catalog.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieResponseDTO {

    private Long id;
    private String title;
    private Integer releaseYear;
    private Double rating;

    private DirectorResponseDTO director;
    private Set<GenreResponseDTO> genres;
}