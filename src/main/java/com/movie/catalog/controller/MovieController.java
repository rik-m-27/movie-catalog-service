package com.movie.catalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/movies", produces = "application/json")
@Tag(
    name = "Movie Management",
    description = "APIs for managing movies, filtering, and genre associations"
)
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(
        summary = "Create a new movie",
        description = "Creates a movie entry with title, release year, rating, director, and associated genres"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponseDTO createMovie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Movie creation payload including title, directorId, rating, and genreIds"
            )
            @Valid @RequestBody MovieRequestDTO request) {
        return movieService.create(request);
    }


    @Operation(
        summary = "Get movie by ID",
        description = "Fetch detailed movie information including director and genres"
    )
    @GetMapping("/{id}")
    public MovieResponseDTO getMovieById(@PathVariable Long id) {
        return movieService.getById(id);
    }


    @Operation(
        summary = "Get all movies",
        description = "Returns list of movies. Supports optional filtering by genre name and release year"
    )
    @GetMapping
    public List<MovieResponseDTO> getAllMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year) {

        return movieService.getAll(genre, year);
    }


    @Operation(
        summary = "Delete movie",
        description = "Permanently removes a movie from the catalog"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
    }

    @Operation(
        summary = "Update movie details",
        description = "Updates movie attributes such as title, rating, director, and genres"
    )
    @PutMapping("/{id}")
    public MovieResponseDTO updateMovie(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated movie payload"
            )
            @Valid @RequestBody MovieRequestDTO request) {
        return movieService.update(id, request);
    }


    @Operation(
        summary = "Add genre to movie",
        description = "Associates an existing genre with a movie and returns the updated movie details"
    )
    @PostMapping("/{id}/genres/{gid}")
    public MovieResponseDTO addGenreToMovie(@PathVariable Long id, @PathVariable Long gid) {
        return movieService.addGenreToMovie(id, gid);
    }

    @Operation(
        summary = "Remove genre from movie",
        description = "Removes an associated genre from a movie"
    )
    @DeleteMapping("/{id}/genres/{gid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGenreFromMovie(@PathVariable Long id, @PathVariable Long gid) {
        movieService.removeGenreFromMovie(id, gid);
    }

}