package com.movie.catalog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DirectorResponseDTO {
    private Long id;
    private String name;
    private String nationality;
    private Integer birthYear;
}