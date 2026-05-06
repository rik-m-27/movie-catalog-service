package com.movie.catalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponseDTO create(@Valid @RequestBody MovieRequestDTO request) {
        return movieService.create(request);
    }

    @GetMapping("/{id}")
    public MovieResponseDTO getById(@PathVariable Long id) {
        return movieService.getById(id);
    }

    @GetMapping
    public List<MovieResponseDTO> getAll(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year) {

        return movieService.getAll(genre, year);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

    @PutMapping("/{id}")
    public MovieResponseDTO update(@PathVariable Long id, @Valid @RequestBody MovieRequestDTO request) {
        return movieService.update(id, request);
    }

    @PostMapping("/{id}/genres/{gid}")
    public void addGenre(@PathVariable Long id, @PathVariable Long gid) {
        movieService.addGenreToMovie(id, gid);
    }

    @DeleteMapping("/{id}/genres/{gid}")
    public void removeGenre(@PathVariable Long id, @PathVariable Long gid) {
        movieService.removeGenreFromMovie(id, gid);
    }

}