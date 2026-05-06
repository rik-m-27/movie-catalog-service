package com.movie.catalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.dto.GenreRequestDTO;
import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.service.GenreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/genres", produces = "application/json")
@Tag(
    name = "Genre Management",
    description = "APIs for managing movie genres"
)
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(
        summary = "Create a new genre",
        description = "Creates a movie genre such as Action, Drama, Thriller, or Comedy."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreResponseDTO createGenre(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Genre creation payload (e.g., Action, Drama)"
            )
            @Valid @RequestBody GenreRequestDTO dto) {
        return genreService.create(dto);
    }

    @Operation(
        summary = "Get all genres",
        description = "Returns list of all available movie genres in the system"
    )
    @GetMapping
    public List<GenreResponseDTO> getAllGenres() {
        return genreService.getAll();
    }
}