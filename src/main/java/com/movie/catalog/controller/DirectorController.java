package com.movie.catalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.service.DirectorService;
import com.movie.catalog.service.MovieService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/directors")
@Tag(
    name = "Director Management",
    description = "APIs for managing directors and retrieving their associated movies"
)
public class DirectorController {

    private final DirectorService directorService;
    private final MovieService movieService;

    public DirectorController(DirectorService directorService, MovieService movieService) {
        this.directorService = directorService;
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a director")
    public DirectorResponseDTO createDirector(@RequestBody @Valid DirectorRequestDTO request) {
        return directorService.create(request);
    }

    @GetMapping
    @Operation(summary = "Get all directors")
    public List<DirectorResponseDTO> getAllDirectors() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get director by ID")
    public DirectorResponseDTO getDirectorById(@PathVariable Long id) {
        return directorService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update director details")
    public DirectorResponseDTO updateDirector(
            @PathVariable Long id,
            @RequestBody @Valid DirectorRequestDTO request) {
        return directorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete director")
    public void deleteDirector(@PathVariable Long id) {
        directorService.delete(id);
    }

    @GetMapping("/{id}/movies")
    @Operation(summary = "Get all movies by director")
    public List<MovieResponseDTO> getMoviesByDirector(@PathVariable Long id) {
        return movieService.getMoviesByDirector(id);
    }
}