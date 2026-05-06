package com.movie.catalog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.movie.catalog.config.CatalogConfig;
import com.movie.catalog.dto.GenreResponseDTO;
import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.entity.Director;
import com.movie.catalog.entity.Genre;
import com.movie.catalog.entity.Movie;
import com.movie.catalog.exception.BadRequestException;
import com.movie.catalog.exception.ResourceNotFoundException;
import com.movie.catalog.repository.DirectorRepository;
import com.movie.catalog.repository.GenreRepository;
import com.movie.catalog.repository.MovieRepository;
import com.movie.catalog.service.impl.MovieServiceImpl;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private CatalogConfig catalogConfig;

    @InjectMocks
    private MovieServiceImpl service;

    
    @Test
    void create_success() {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .title("Iron Man")
                .releaseYear(2008)
                .rating(8.8)
                .directorId(1L)
                .genreIds(Set.of(1L, 2L))
                .build();

        Director director = new Director();
        director.setId(1L);

        Genre g1 = new Genre(1L, "Sci-Fi", null);
        Genre g2 = new Genre(2L, "Action", null);

        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
        when(genreRepository.findAllById(request.getGenreIds()))
                .thenReturn(List.of(g1, g2));
        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);

        Movie savedMovie = new Movie();
        savedMovie.setId(100L);
        savedMovie.setTitle(request.getTitle());
        savedMovie.setReleaseYear(request.getReleaseYear());
        savedMovie.setRating(request.getRating());
        savedMovie.setDirector(director);
        savedMovie.setGenres(Set.of(g1, g2));

        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        MovieResponseDTO response = service.create(request);

        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(100L, response.getId());

        verify(movieRepository).save(any(Movie.class));
    }


    @Test
    void create_directorNotFound() {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .directorId(1L)
                .genreIds(Set.of(1L))
                .build();

        when(directorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.create(request));

        verify(directorRepository).findById(1L);
        verify(movieRepository, never()).save(any());
    }

    
    @Test
    void create_invalidGenres() {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .directorId(1L)
                .genreIds(Set.of(1L, 2L))
                .build();

        Director director = new Director();
        director.setId(1L);

        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
        when(genreRepository.findAllById(request.getGenreIds()))
                .thenReturn(List.of(new Genre(1L, "Action", null))); // only 1 found

        assertThrows(BadRequestException.class,
                () -> service.create(request));
    }

    
    @Test
    void create_exceedsMaxGenres() {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .directorId(1L)
                .genreIds(Set.of(1L, 2L, 3L))
                .build();

        Director director = new Director();
        director.setId(1L);

        List<Genre> genres = List.of(
                new Genre(1L, "Rohit", null),
                new Genre(2L, "Virat", null),
                new Genre(3L, "Sanju", null)
        );

        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
        when(genreRepository.findAllById(request.getGenreIds()))
                .thenReturn(genres);
        when(catalogConfig.maxGenresPerMovie()).thenReturn(2);

        assertThrows(BadRequestException.class,
                () -> service.create(request));
    }

    
    @Test
    void create_defaultRatingApplied() {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .title("Test")
                .releaseYear(2020)
                .directorId(1L)
                .genreIds(Set.of(1L))
                .build();

        Director director = new Director();
        director.setId(1L);

        Genre g1 = new Genre(1L, "Action", null);

        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
        when(genreRepository.findAllById(request.getGenreIds()))
                .thenReturn(List.of(g1));
        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);
        when(catalogConfig.defaultRating()).thenReturn(5.0);

        Movie saved = new Movie();
        saved.setId(10L);
        saved.setRating(5.0);
        saved.setDirector(director);
        saved.setGenres(Set.of(g1));

        when(movieRepository.save(any())).thenReturn(saved);

        MovieResponseDTO response = service.create(request);

        assertEquals(5.0, response.getRating());
    }

    
    @Test
    void getById_success() {

        Director director = new Director();
        director.setId(1L);
        director.setName("Rohit");

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDirector(director);
        movie.setGenres(Set.of());

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieResponseDTO response = service.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(movieRepository).findById(1L);
    }

    
    @Test
    void getById_notFound() {

        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void getAll_withFilters_shouldReturnFilteredMovies() {
        
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Iron Man");
        movie.setReleaseYear(2008);
        movie.setDirector(new Director(1L, "Robert", "American", 1975, new ArrayList<>()));
        movie.setGenres(Set.of());

        when(movieRepository.findByFilters("Sci-Fi", 2008))
                .thenReturn(List.of(movie));

        List<MovieResponseDTO> result = service.getAll("Sci-Fi", 2008);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(movie.getTitle(), result.get(0).getTitle());
        verify(movieRepository).findByFilters("Sci-Fi", 2008);
    }

    @Test
    void getAll_noFilters_shouldReturnAllMovies() {
        
        Movie m1 = new Movie();
        m1.setId(1L);
        m1.setTitle("Iron Man");
        m1.setReleaseYear(2008);
        m1.setDirector(new Director(1L, "Robert", "American", 1975, new ArrayList<>()));
        m1.setGenres(Set.of());

        Movie m2 = new Movie();
        m2.setId(2L);
        m2.setTitle("Iron Man 2");
        m2.setReleaseYear(2008);
        m2.setDirector(new Director(1L, "Robert", "American", 1975, new ArrayList<>()));
        m2.setGenres(Set.of());

        when(movieRepository.findByFilters(null, null))
                .thenReturn(List.of(m1, m2));

        
        List<MovieResponseDTO> result = service.getAll(null, null);

        
        assertEquals(2, result.size());
        verify(movieRepository).findByFilters(null, null);
    }


    @Test
    void update_success() {

        Long id = 1L;
        MovieRequestDTO request = MovieRequestDTO.builder()
                .title("Captain America")
                .releaseYear(2009)
                .rating(9.0)
                .directorId(3L)
                .genreIds(Set.of(1L))
                .build();

        Director director = new Director();
        director.setId(3L);

        Genre genre = new Genre(1L, "Action", new HashSet<>());

        Movie existing = new Movie();
        existing.setId(id);
        existing.setTitle("Iron Man");
            
        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);

        when(movieRepository.findById(id)).thenReturn(Optional.of(existing));
        when(directorRepository.findById(request.getDirectorId())).thenReturn(Optional.of(director));
        when(genreRepository.findAllById(request.getGenreIds()))
                .thenReturn(List.of(genre));


        when(movieRepository.save(any())).thenAnswer(updatedMovies -> updatedMovies.getArgument(0));

        MovieResponseDTO response = service.update(id, request);

        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getDirectorId(), response.getDirector().getId());
        assertEquals(request.getReleaseYear(), response.getReleaseYear());
        assertEquals(request.getRating(), response.getRating());
        assertNotNull(response.getGenres());
        Set<Long> resultGenreIds = response.getGenres().stream()
                .map(GenreResponseDTO::getId)
                .collect(Collectors.toSet());
        assertEquals(request.getGenreIds(), resultGenreIds);

        verify(catalogConfig).maxGenresPerMovie();
        verify(movieRepository).findById(id);
        verify(directorRepository).findById(request.getDirectorId());
        verify(genreRepository).findAllById(request.getGenreIds());
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void update_success_whenRequestRatingIsNull() {
        // Arrange
        Long id = 1L;
        MovieRequestDTO request = MovieRequestDTO.builder()
                .title("Avengers")
                .releaseYear(2012)
                .rating(null)
                .directorId(3L)
                .genreIds(Set.of(1L))
                .build();

        Movie existing = new Movie();
        existing.setId(id);

        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);
        when(catalogConfig.defaultRating()).thenReturn(7.5);

        when(movieRepository.findById(id)).thenReturn(Optional.of(existing));
        when(directorRepository.findById(any())).thenReturn(Optional.of(new Director()));
        when(genreRepository.findAllById(any())).thenReturn(List.of(new Genre(1L, "Action", null)));
        when(movieRepository.save(any())).thenAnswer(updatedMovies -> updatedMovies.getArgument(0));

        MovieResponseDTO response = service.update(id, request);

        assertEquals(7.5, response.getRating(), "Should have used the default rating from config");
        verify(catalogConfig).defaultRating();
    }

    @Test
    void update_movieNotFound() {

        MovieRequestDTO request = MovieRequestDTO.builder()
                .directorId(3L)
                .genreIds(Set.of(12L))
                .build();

        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);

        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(1L, request));
        
        verify(movieRepository).findById(1L);
    }

    @Test
    void update_directorNotFound() {

        Movie existing = new Movie();
        existing.setId(1L);

        MovieRequestDTO request = MovieRequestDTO.builder()
                .directorId(3L)
                .genreIds(Set.of(12L))
                .build();

        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);
        when(directorRepository.findById(3L)).thenReturn(Optional.empty());
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(1L, request));
        
        verify(catalogConfig).maxGenresPerMovie();
        verify(movieRepository).findById(1L);
        verify(directorRepository).findById(3L);
    }


    @Test
    void update_invalidGenres() {

        Long id = 1L;
        Movie existing = new Movie();
        existing.setId(1L);

        MovieRequestDTO request = MovieRequestDTO.builder()
                .directorId(3L)
                .genreIds(Set.of(11L, 12L))
                .build();

        Director director = new Director();
        director.setId(3L);

        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);
        when(directorRepository.findById(request.getDirectorId())).thenReturn(Optional.of(director));
        when(movieRepository.findById(id)).thenReturn(Optional.of(existing));

        when(genreRepository.findAllById(request.getGenreIds()))
                .thenReturn(List.of(new Genre(11L, "Action", null)));

        assertThrows(BadRequestException.class,
                () -> service.update(id, request));

        verify(catalogConfig).maxGenresPerMovie();
        verify(movieRepository).findById(id);
        verify(directorRepository).findById(request.getDirectorId());
        verify(genreRepository).findAllById(request.getGenreIds());
    }

    @Test
    void update_exceedsMaxGenres() {
        
        Long id = 1L;
        
        MovieRequestDTO request = MovieRequestDTO.builder()
                .genreIds(Set.of(1L, 2L, 3L)) 
                .build();

        when(catalogConfig.maxGenresPerMovie()).thenReturn(2);

        assertThrows(BadRequestException.class,
                () -> service.update(id, request));

        verify(movieRepository, never()).findById(id);
    }


    @Test
    void delete_success() {

        Long id = 1L;

        when(movieRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(movieRepository).existsById(id);
        verify(movieRepository).deleteById(id);
    }

    @Test
    void delete_notFound() {

        Long id = 1L;

        when(movieRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.delete(id));

        verify(movieRepository).existsById(id);
        verify(movieRepository, never()).deleteById(any());
    }

    @Test
    void getMoviesByDirector_success() {

        when(directorRepository.existsById(1L)).thenReturn(true);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDirector(new Director());
        movie.setGenres(Set.of());

        when(movieRepository.findByDirectorId(1L))
                .thenReturn(List.of(movie));

        List<MovieResponseDTO> result = service.getMoviesByDirector(1L);

        assertEquals(1, result.size());

        verify(movieRepository).findByDirectorId(1L);
    }

    @Test
    void getMoviesByDirector_notFound() {

        when(directorRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.getMoviesByDirector(1L));

        verify(directorRepository).existsById(1L);
        verify(movieRepository, never()).findByDirectorId(any());
    }

    @Test
    void addGenreToMovie_success() {

        Movie movie = new Movie();
        movie.setGenres(new HashSet<>());

        Genre genre = new Genre();
        genre.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);

        service.addGenreToMovie(1L, 1L);

        assertTrue(movie.getGenres().contains(genre));
        verify(movieRepository).save(movie);
    }

    @Test
    void addGenreToMovie_movieNotFound() {
       
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> service.addGenreToMovie(1L, 99L));
        
        verify(movieRepository).findById(1L);
        verifyNoInteractions(genreRepository);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void addGenreToMovie_alreadyPresent() {
        // Arrange
        Genre genre = new Genre(1L, "Action", new HashSet<>());
        Movie movie = new Movie();
        movie.setGenres(new HashSet<>(Set.of(genre)));

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        
        service.addGenreToMovie(1L, 1L);

        assertEquals(1, movie.getGenres().size());
        
        verify(movieRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void addGenreToMovie_genreNotFound() {
       
        Movie movie = new Movie();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(catalogConfig.maxGenresPerMovie()).thenReturn(5);
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> service.addGenreToMovie(1L, 99L));
        
        verify(movieRepository).findById(1L);
        verify(genreRepository).findById(99L);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void addGenreToMovie_exceedsMaxLimit(){

        Long mid = 11L;
        Long gid = 99L;
        Movie movie = new Movie();
        movie.setGenres(Set.of(new Genre(1L, "Action", null), new Genre(2L, "Drama", null)));

        when(movieRepository.findById(mid)).thenReturn(Optional.of(movie));
        when(catalogConfig.maxGenresPerMovie()).thenReturn(2);

        assertThrows(BadRequestException.class, 
            () -> service.addGenreToMovie(mid, gid));
        
        verify(movieRepository).findById(mid);
        verifyNoInteractions(genreRepository);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void removeGenre_success() {

        Long mid = 1L;
        Long gid = 5L;

        Genre genre = new Genre();
        genre.setId(gid);

        Movie movie = new Movie();
        movie.setGenres(new HashSet<>(Set.of(genre)));

        when(movieRepository.findById(mid)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(gid)).thenReturn(Optional.of(genre));

        service.removeGenreFromMovie(mid, gid);

        assertFalse(movie.getGenres().contains(genre));
        verify(movieRepository).findById(mid);
        verify(genreRepository).findById(gid);
        verify(movieRepository).save(movie);
    }

    @Test
    void removeGenreFromMovie_movieNotFound() {
        Long mid = 1L;
        Long gid = 5L;

        when(movieRepository.findById(mid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> service.removeGenreFromMovie(mid, gid));
        
        verify(movieRepository).findById(mid);
        verifyNoInteractions(genreRepository);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void removeGenreFromMovie_genreNotFound() {
        Long mid = 1L;
        Long gid = 99L;

        Movie movie = new Movie();
        when(movieRepository.findById(mid)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(gid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> service.removeGenreFromMovie(mid, gid));
        
        verify(genreRepository).findById(gid);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void removeGenreFromMovie_genreNotPresent() {
        Long mid = 1L;
        Long gid = 5L;

        Genre genre = new Genre();
        genre.setId(gid);

        Movie movie = new Movie();
        movie.setGenres(new HashSet<>());

        when(movieRepository.findById(mid)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(gid)).thenReturn(Optional.of(genre));

        service.removeGenreFromMovie(mid, gid);

        verify(movieRepository).findById(mid);
        verify(genreRepository).findById(gid);
        verify(movieRepository, never()).save(any());
    }
}