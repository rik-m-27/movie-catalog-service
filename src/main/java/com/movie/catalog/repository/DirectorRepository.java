package com.movie.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.catalog.entity.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

}