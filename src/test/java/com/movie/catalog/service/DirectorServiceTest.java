package com.movie.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;
import com.movie.catalog.entity.Director;
import com.movie.catalog.exception.ResourceNotFoundException;
import com.movie.catalog.repository.DirectorRepository;
import com.movie.catalog.service.impl.DirectorServiceImpl;


@ExtendWith(MockitoExtension.class)
public class DirectorServiceTest {

    @Mock
    private DirectorRepository repository;

    @InjectMocks
    private DirectorServiceImpl service;

    @Test
    void createDirector_success() {
        
        DirectorRequestDTO request = DirectorRequestDTO.builder()
                                        .name("Aditya Dhar")
                                        .birthYear(1980)
                                        .nationality("Indian")
                                        .build();
        Director savedDirector = new Director(1L, request.getName(), request.getNationality(), request.getBirthYear(), new ArrayList<>());

        when(repository.save(any(Director.class))).thenReturn(savedDirector);

        DirectorResponseDTO response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getBirthYear(), response.getBirthYear());
        assertEquals(request.getNationality(), response.getNationality());

        verify(repository).save(any(Director.class));

    }

    @Test
    void getAll_success() {
        
        Director d1 = new Director();
        d1.setId(1L);
        d1.setName("Rohit");

        Director d2 = new Director();
        d2.setId(2L);
        d2.setName("Virat");

        when(repository.findAll()).thenReturn(List.of(d1, d2));

        List<DirectorResponseDTO> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals(d1.getName(), result.get(0).getName());
        assertEquals(d2.getName(), result.get(1).getName());

        verify(repository).findAll();
    }


    @Test
    void getAll_emptyList() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<DirectorResponseDTO> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void getById_success(){

        Long id = 1L;

        Director director = new Director();
        director.setId(id);
        director.setName("Rohit");

        when(repository.findById(1L)).thenReturn(Optional.of(director));

        DirectorResponseDTO response = service.getById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(director.getName() ,response.getName());

        verify(repository).findById(id);
    }

    @Test
    void getById_notFound(){

        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(id));

        verify(repository).findById(id);
    }

    @Test
    void update_success(){

        Long id = 1L;

        DirectorRequestDTO request = DirectorRequestDTO.builder()
                                        .name("Rohit Sharma")
                                        .birthYear(1990)
                                        .nationality("Indian")
                                        .build();
        Director fetchDirector = new Director(id, "Aditya Dhar", "British", 1988, new ArrayList<>());

        when(repository.findById(id)).thenReturn(Optional.of(fetchDirector));

        when(repository.save(fetchDirector)).thenReturn(fetchDirector);

        DirectorResponseDTO response = service.update(id, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getBirthYear(), response.getBirthYear());
        assertEquals(request.getNationality(), response.getNationality());

        verify(repository).findById(id);
        verify(repository).save(fetchDirector);
    }
    

    @Test
    void update_notFound(){

        Long id = 1L;

        DirectorRequestDTO request = DirectorRequestDTO.builder()
                                        .name("Rohit Sharma")
                                        .birthYear(1990)
                                        .nationality("Indian")
                                        .build();

        when(repository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.update(id, request));

        verify(repository).findById(id);
        verify(repository, never()).save(any(Director.class));
    }


    @Test
    void delete_success(){
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);
        
        service.delete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_notFound(){
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));

        verify(repository).existsById(id);
        verify(repository, never()).deleteById(id);
    }
}
