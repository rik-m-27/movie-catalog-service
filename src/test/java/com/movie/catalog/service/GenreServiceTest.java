package com.movie.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.movie.catalog.dto.GenreRequestDTO;
import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.entity.Genre;
import com.movie.catalog.exception.BadRequestException;
import com.movie.catalog.repository.GenreRepository;
import com.movie.catalog.service.impl.GenreServiceImpl;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository repository;

    @InjectMocks
    private GenreServiceImpl service;


    @Test
    void create_success() {

        GenreRequestDTO request = GenreRequestDTO.builder()
                .name("Action")
                .build();

        Genre savedGenre = new Genre();
        savedGenre.setId(1L);
        savedGenre.setName(request.getName());

        when(repository.findByName("Action")).thenReturn(Optional.empty());
        when(repository.save(any(Genre.class))).thenReturn(savedGenre);

        GenreResponseDTO response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Action", response.getName());

        verify(repository).findByName(request.getName());
        verify(repository).save(any(Genre.class));
    }

    @Test
    void create_duplicate() {

        GenreRequestDTO request = GenreRequestDTO.builder()
                .name("Action")
                .build();

        when(repository.findByName(request.getName()))
                .thenReturn(Optional.of(new Genre()));

        assertThrows(BadRequestException.class,
                () -> service.create(request));

        verify(repository).findByName("Action");
        verify(repository, never()).save(any());
    }

    @Test
    void getAll_success() {

        Genre g1 = new Genre();
        g1.setId(1L);
        g1.setName("Action");

        Genre g2 = new Genre();
        g2.setId(2L);
        g2.setName("Drama");

        when(repository.findAll()).thenReturn(List.of(g1, g2));

        List<GenreResponseDTO> response = service.getAll();

        assertNotNull(response);
        assertEquals(2, response.size());

        verify(repository).findAll();
    }

    @Test
    void getAll_empty() {

        when(repository.findAll()).thenReturn(List.of());

        List<GenreResponseDTO> response = service.getAll();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(repository).findAll();
    }
}
