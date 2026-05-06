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
@RequestMapping(path = "/api/directors", produces = "application/json")
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

    @Operation(
        summary = "Create a new director",
        description = "Creates a director entry with name, nationality, and birth year."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorResponseDTO createDirector(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Director creation payload"
            )
            @RequestBody @Valid DirectorRequestDTO request) {
        return directorService.create(request);
    }

    @Operation(
        summary = "Get all directors",
        description = "Returns a list of all directors in the system"
    )
    @GetMapping
    public List<DirectorResponseDTO> getAllDirectors() {
        return directorService.getAll();
    }

    @Operation(
        summary = "Get director by ID",
        description = "Fetches a single director along with basic profile information"
    )
    @GetMapping("/{id}")
    public DirectorResponseDTO getDirectorById(@PathVariable Long id) {
        return directorService.getById(id);
    }

    @Operation(
        summary = "Update director details",
        description = "Updates existing director information by ID"
    )
    @PutMapping("/{id}")
    public DirectorResponseDTO updateDirector(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated director payload"
            )
            @RequestBody @Valid DirectorRequestDTO request) {
        return directorService.update(id, request);
    }

    @Operation(
        summary = "Delete director",
        description = "Removes a director permanently from the system"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDirector(@PathVariable Long id) {
        directorService.delete(id);
    }
    @Operation(
        summary = "Get movies by director",
        description = "Fetches all movies associated with a given director ID"
    )
    @GetMapping("/{id}/movies")
    public List<MovieResponseDTO> getMoviesByDirector(@PathVariable Long id) {
        return movieService.getMoviesByDirector(id);
    }
    
}