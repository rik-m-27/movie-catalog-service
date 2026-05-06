package com.movie.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movie.catalog.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{

    @Query("""
        SELECT m FROM Movie m
        WHERE (:year IS NULL OR m.releaseYear = :year)
        AND (:genre IS NULL OR EXISTS (
            SELECT 1 FROM m.genres g WHERE g.name = :genre
        ))
    """)
    List<Movie> findByFilters(@Param("genre") String genre, @Param("year") Integer year);

    List<Movie> findByDirectorId(Long directorId);

}
