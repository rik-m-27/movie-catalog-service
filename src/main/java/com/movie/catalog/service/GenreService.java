package com.movie.catalog.service;

import java.util.List;

import com.movie.catalog.dto.GenreRequestDTO;
import com.movie.catalog.dto.GenreResponseDTO;

public interface GenreService {

    GenreResponseDTO create(GenreRequestDTO request);

    List<GenreResponseDTO> getAll();
}