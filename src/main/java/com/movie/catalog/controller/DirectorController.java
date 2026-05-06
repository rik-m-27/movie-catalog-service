package com.movie.catalog.controller;

import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.service.DirectorService;
import com.movie.catalog.service.MovieService;

import jakarta.validation.Valid;

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

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;
    private final MovieService movieService;

    public DirectorController(DirectorService directorService, MovieService movieService) {
        this.directorService = directorService;
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorResponseDTO create(@RequestBody @Valid DirectorRequestDTO request) {
        return directorService.create(request);
    }

    @GetMapping
    public List<DirectorResponseDTO> getAll() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public DirectorResponseDTO getById(@PathVariable Long id) {
        return directorService.getById(id);
    }

    @PutMapping("/{id}")
    public DirectorResponseDTO update(@PathVariable Long id,
                                      @RequestBody @Valid DirectorRequestDTO request) {
        return directorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
    }

    @GetMapping("/{id}/movies")
    public List<MovieResponseDTO> getMoviesByDirector(@PathVariable Long id) {
        return movieService.getMoviesByDirector(id);
    }
}