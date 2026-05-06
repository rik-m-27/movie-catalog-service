package com.movie.catalog.mapper;

import java.util.stream.Collectors;

import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.entity.Movie;

public final class MovieMapper {

    private MovieMapper() {

    }

    public static Movie requestToMovie(MovieRequestDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setRating(dto.getRating());
        movie.setReleaseYear(dto.getReleaseYear());
        return movie;
    }

    public static MovieResponseDTO movieToResponse(Movie movie) {
        return MovieResponseDTO.builder()
                .title(movie.getTitle())
                .id(movie.getId())
                .rating(movie.getRating())
                .releaseYear(movie.getReleaseYear())
                .director(DirectorMapper.directorToResponse(movie.getDirector()))
                .genres(movie.getGenres()
                        .stream()
                        .map(GenreMapper::genreToResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

}
