package com.movie.catalog.controller;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.catalog.dto.DirectorRequestDTO;
import com.movie.catalog.dto.DirectorResponseDTO;
import com.movie.catalog.dto.GenreRequestDTO;
import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.repository.DirectorRepository;
import com.movie.catalog.repository.GenreRepository;
import com.movie.catalog.repository.MovieRepository;


@SpringBootTest
@AutoConfigureMockMvc
class MovieIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        directorRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    void fullFlow_createMovie_andFetch_success() throws Exception {

        DirectorResponseDTO director = createDirector("Aditya Dhar", "Indian", 1980);
        GenreResponseDTO genre = createGenre("Action");

        MovieResponseDTO movie = createMovie(
                director.getId(),
                genre.getId(),
                "Dhurandhar",
                2025
        );

        mockMvc.perform(get("/api/movies/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Dhurandhar"))
                .andExpect(jsonPath("$.director.id").value(director.getId()))
                .andExpect(jsonPath("$.genres[0].id").value(genre.getId()));
    }


    @Test
    void createMovie_validationFailure_shouldReturn400() throws Exception {

        DirectorResponseDTO director = createDirector("Robert", "American", 1960);
        GenreResponseDTO genre = createGenre("Drama");

        MovieRequestDTO invalidRequest = MovieRequestDTO.builder()
                .title("")
                .releaseYear(2025)
                .directorId(director.getId())
                .genreIds(Set.of(genre.getId()))
                .build();

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    void deleteMovie_fullFlow_success() throws Exception {

        DirectorResponseDTO director = createDirector("Aditya Dhar", "Indian", 1980);
        GenreResponseDTO genre = createGenre("Action");

        MovieResponseDTO movie = createMovie(
                director.getId(),
                genre.getId(),
                "Rocky",
                2016
        );

        mockMvc.perform(delete("/api/movies/" + movie.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/movies/" + movie.getId()))
                .andExpect(status().isNotFound());
    }


    @Test
    void getMovies_withFilters_shouldReturnOnlyMatchingResults() throws Exception {
        
        DirectorResponseDTO director = createDirector("Aditya Dhar", "Indian", 1980);
        GenreResponseDTO action = createGenre("Action");
        GenreResponseDTO comedy = createGenre("Comedy");

        createMovie(director.getId(), action.getId(), "Dhurandhar", 2025); 

        createMovie(director.getId(), action.getId(), "Dhurandhar 2", 2026); 
        
        createMovie(director.getId(), comedy.getId(), "Hera Pheri", 2003); 

        mockMvc.perform(get("/api/movies")
                        .param("genre", "Action")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // Only 1 movie should return
                .andExpect(jsonPath("$[0].title").value("Dhurandhar"))
                .andExpect(jsonPath("$[0].genres[0].name").value("Action"));
    }


    private DirectorResponseDTO createDirector(String name, String nationality, int birthYear) throws Exception{

        DirectorRequestDTO directorRequest = DirectorRequestDTO.builder()
                .name(name)
                .nationality(nationality)
                .birthYear(birthYear)
                .build();

        String directorResponse = mockMvc.perform(post("/api/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(directorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(directorRequest.getName()))
                .andExpect(jsonPath("$.birthYear").value(directorRequest.getBirthYear()))
                .andExpect(jsonPath("$.nationality").value(directorRequest.getNationality()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(directorResponse, DirectorResponseDTO.class);
    }

    private GenreResponseDTO createGenre(String name) throws Exception{

        GenreRequestDTO request = GenreRequestDTO.builder()
                .name(name)
                .build();

        String response = mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, GenreResponseDTO.class);
    }

    private MovieResponseDTO createMovie(Long directorId, Long genreId, String title, Integer releaseYear) throws Exception {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .title(title)
                .releaseYear(releaseYear)
                .rating(9.0)
                .directorId(directorId)
                .genreIds(Set.of(genreId))
                .build();

        String response = mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(title))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, MovieResponseDTO.class);
    }
    
}