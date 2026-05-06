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

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request payload for creating or updating a movie")
public class MovieRequestDTO {

    @NotBlank
    @Schema(example = "URI: The Surgical Strike")
    private String title;

    @NotNull
    @Schema(example = "2019")
    private Integer releaseYear;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Schema(example = "8.2")
    private Double rating;

    @NotNull
    @Schema(example = "1")
    private Long directorId;

    @Builder.Default
    @Schema(
        description = "List of genre IDs associated with the movie",
        example = "[1, 2]"
    )
    private Set<Long> genreIds = new HashSet<>();


    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setGenreIds(Set<Long> genreIds) {
        this.genreIds = (genreIds == null) ? new HashSet<>() : genreIds;
    }
}