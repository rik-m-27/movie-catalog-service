package com.movie.catalog.mapper;

import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;
import com.movie.catalog.entity.Director;

public final class DirectorMapper {

    private DirectorMapper() {
    }

    public static Director requestToDirector(DirectorRequestDTO dto) {
        Director director = new Director();
        director.setName(dto.getName());
        director.setNationality(dto.getNationality());
        director.setBirthYear(dto.getBirthYear());
        return director;
    }

    public static DirectorResponseDTO directorToResponse(Director director) {
        return DirectorResponseDTO.builder()
                .id(director.getId())
                .name(director.getName())
                .nationality(director.getNationality())
                .birthYear(director.getBirthYear())
                .build();
    }
}