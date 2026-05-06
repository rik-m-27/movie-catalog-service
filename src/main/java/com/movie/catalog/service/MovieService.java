package com.movie.catalog.service;

import java.util.List;

import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;

public interface MovieService {

    MovieResponseDTO create(MovieRequestDTO request);
    MovieResponseDTO getById(Long id);
    List<MovieResponseDTO> getAll(String genre, Integer year);
    MovieResponseDTO update(Long id, MovieRequestDTO request);
    void delete(Long id);
    List<MovieResponseDTO> getMoviesByDirector(Long directorId);
    MovieResponseDTO addGenreToMovie(Long movieId, Long genreId);
    void removeGenreFromMovie(Long movieId, Long genreId);
}
