package com.movie.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movie.catalog.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{

    @Query("""
        SELECT DISTINCT m FROM Movie m
        LEFT JOIN m.genres g
        WHERE (:year IS NULL OR m.releaseYear = :year)
        AND (:genre IS NULL OR g.name = :genre)
    """)
    List<Movie> findByFilters(@Param("genre") String genre, @Param("year") Integer year);

    List<Movie> findByDirectorId(Long directorId);

}
