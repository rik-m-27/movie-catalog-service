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
@RequestMapping("/api/movies")
@Tag(
    name = "Movie Management",
    description = "APIs for managing movies, filtering, and genre associations"
)
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create movie")
    public MovieResponseDTO createMovie(@Valid @RequestBody MovieRequestDTO request) {
        return movieService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by id")
    public MovieResponseDTO getMovieById(@PathVariable Long id) {
        return movieService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all movies (with optional filters)")
    public List<MovieResponseDTO> getAllMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year) {

        return movieService.getAll(genre, year);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete movie")
    public void deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update movie")
    public MovieResponseDTO updateMovie(@PathVariable Long id,
                                        @Valid @RequestBody MovieRequestDTO request) {
        return movieService.update(id, request);
    }

    @PostMapping("/{id}/genres/{gid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add genre to movie")
    public void addGenreToMovie(@PathVariable Long id, @PathVariable Long gid) {
        movieService.addGenreToMovie(id, gid);
    }

    @DeleteMapping("/{id}/genres/{gid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove genre from movie")
    public void removeGenreFromMovie(@PathVariable Long id, @PathVariable Long gid) {
        movieService.removeGenreFromMovie(id, gid);
    }
}