package com.movie.catalog.service;

import java.util.List;

import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;

public interface DirectorService {
    DirectorResponseDTO create(DirectorRequestDTO request);
    List<DirectorResponseDTO> getAll();
    DirectorResponseDTO getById(Long id);
    DirectorResponseDTO update(Long id, DirectorRequestDTO request);
    void delete(Long id);
}