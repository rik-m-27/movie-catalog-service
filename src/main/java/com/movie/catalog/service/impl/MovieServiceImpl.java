package com.movie.catalog.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.movie.catalog.config.CatalogConfig;
import com.movie.catalog.dto.MovieRequestDTO;
import com.movie.catalog.dto.MovieResponseDTO;
import com.movie.catalog.entity.Director;
import com.movie.catalog.entity.Genre;
import com.movie.catalog.entity.Movie;
import com.movie.catalog.exception.BadRequestException;
import com.movie.catalog.exception.ResourceNotFoundException;
import com.movie.catalog.mapper.MovieMapper;
import com.movie.catalog.repository.DirectorRepository;
import com.movie.catalog.repository.GenreRepository;
import com.movie.catalog.repository.MovieRepository;
import com.movie.catalog.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final CatalogConfig catalogConfig;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository, GenreRepository genreRepository,
            CatalogConfig catalogConfig) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
        this.genreRepository = genreRepository;
        this.catalogConfig = catalogConfig;
    }

    @Override
    public MovieResponseDTO create(MovieRequestDTO request) {

        Director director = directorRepository.findById(request.getDirectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Director not found: " + request.getDirectorId()));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(request.getGenreIds()));
        
        if (genres.size() != request.getGenreIds().size()) {
            throw new BadRequestException("Invalid genreIds provided");
        }
        
        if (genres.size() > catalogConfig.maxGenresPerMovie()) {
            throw new BadRequestException("Max " + catalogConfig.maxGenresPerMovie() + " genres allowed");
        }

        Movie movie = MovieMapper.requestToMovie(request);

        movie.setDirector(director);
        movie.setGenres(genres);

        if (movie.getRating() == null) {
            movie.setRating(catalogConfig.defaultRating());
        }

        Movie savedMovie = movieRepository.save(movie);

        return MovieMapper.movieToResponse(savedMovie);
    }

    @Override
    public MovieResponseDTO getById(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));

        return MovieMapper.movieToResponse(movie);
    }


    @Override
    public List<MovieResponseDTO> getAll(String genre, Integer year) {

        List<Movie> movies = movieRepository.findByFilters(genre, year);

        return movies.stream()
                .map(MovieMapper::movieToResponse)
                .toList();
    }

    @Override
    public MovieResponseDTO update(Long id, MovieRequestDTO request) {

        if (request.getGenreIds().size() > catalogConfig.maxGenresPerMovie()) {
            throw new BadRequestException(
                "Max " + catalogConfig.maxGenresPerMovie() + " genres allowed");
        }
        
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));

    
        Director director = directorRepository.findById(request.getDirectorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Director not found: " + request.getDirectorId()));

        
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(request.getGenreIds()));

        if (genres.size() != request.getGenreIds().size()) {
            throw new BadRequestException("Invalid genreIds provided");
        }

        movie.setTitle(request.getTitle());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setRating(request.getRating() != null ? request.getRating() : catalogConfig.defaultRating());
        movie.setDirector(director);
        movie.setGenres(genres);

        Movie updatedMovie = movieRepository.save(movie);

        return MovieMapper.movieToResponse(updatedMovie);
    }

    @Override
    public void delete(Long id) {

        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found: " + id);
        }

        movieRepository.deleteById(id);
    }

    @Override
    public List<MovieResponseDTO> getMoviesByDirector(Long directorId) {

        if (!directorRepository.existsById(directorId)) {
            throw new ResourceNotFoundException("Director not found: " + directorId);
        }

        return movieRepository.findByDirectorId(directorId)
                .stream()
                .map(MovieMapper::movieToResponse)
                .toList();
    }

    @Override
    public void addGenreToMovie(Long movieId, Long genreId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + movieId));

        if (movie.getGenres().size() == catalogConfig.maxGenresPerMovie()) {
            throw new BadRequestException("Max genres limit reached");
        }

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found: " + genreId));

        if (movie.getGenres().contains(genre)) {
            return;
        }

        movie.getGenres().add(genre);

        movieRepository.save(movie);
    }

    @Override
    public void removeGenreFromMovie(Long movieId, Long genreId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + movieId));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found: " + genreId));

        if (!movie.getGenres().contains(genre)) {
            return;
        }

        movie.getGenres().remove(genre);

        movieRepository.save(movie);
    }
}
