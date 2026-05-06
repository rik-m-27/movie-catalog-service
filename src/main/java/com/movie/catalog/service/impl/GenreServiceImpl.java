package com.movie.catalog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.catalog.dto.GenreRequestDTO;
import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.entity.Genre;
import com.movie.catalog.exception.BadRequestException;
import com.movie.catalog.mapper.GenreMapper;
import com.movie.catalog.repository.GenreRepository;
import com.movie.catalog.service.GenreService;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreResponseDTO create(GenreRequestDTO request) {
        if(genreRepository.findByName(request.getName()).isPresent()){
            throw new BadRequestException("Genre already exists");
        }
        Genre genre = GenreMapper.requestToGenre(request);
        Genre savedGenre = genreRepository.save(genre);
        return GenreMapper.genreToResponse(savedGenre);
    }

    @Override
    public List<GenreResponseDTO> getAll() {
        return genreRepository.findAll()
                .stream()
                .map(GenreMapper::genreToResponse)
                .toList();
    }
}