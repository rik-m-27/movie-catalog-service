package com.movie.catalog.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieRequestDTO {

    @NotBlank
    private String title;

    @NotNull
    private Integer releaseYear;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    private Double rating;

    @NotNull
    private Long directorId;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Set<Long> genreIds = new HashSet<>();
}