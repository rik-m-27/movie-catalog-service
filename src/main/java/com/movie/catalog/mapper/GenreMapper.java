package com.movie.catalog.mapper;

import com.movie.catalog.dto.GenreRequestDTO;
import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.entity.Genre;

public final class GenreMapper {

    private GenreMapper() {
    }

    public static Genre requestToGenre(GenreRequestDTO dto) {
        Genre genre = new Genre();
        genre.setName(dto.getName());
        return genre;
    }

    public static GenreResponseDTO genreToResponse(Genre genre) {
        return GenreResponseDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}