package com.movie.catalog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;
import com.movie.catalog.entity.Director;
import com.movie.catalog.exception.ResourceNotFoundException;
import com.movie.catalog.mapper.DirectorMapper;
import com.movie.catalog.repository.DirectorRepository;
import com.movie.catalog.service.DirectorService;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public DirectorResponseDTO create(DirectorRequestDTO request) {
        Director director = DirectorMapper.requestToDirector(request);
        Director savedDirector = directorRepository.save(director);
        return DirectorMapper.directorToResponse(savedDirector);
    }

    @Override
    public List<DirectorResponseDTO> getAll() {
        return directorRepository.findAll()
                .stream()
                .map(DirectorMapper::directorToResponse)
                .toList();
    }

    @Override
    public DirectorResponseDTO getById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director not found: " + id));

        return DirectorMapper.directorToResponse(director);
    }

    @Override
    public DirectorResponseDTO update(Long id, DirectorRequestDTO request) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director not found: " + id));

        director.setName(request.getName());
        director.setNationality(request.getNationality());
        director.setBirthYear(request.getBirthYear());

        return DirectorMapper.directorToResponse(directorRepository.save(director));
    }

    @Override
    public void delete(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Director not found: " + id);
        }
        directorRepository.deleteById(id);
    }
}