package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Genre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findByNameEquals(String name);

    List<Genre> findAllByBaseGenreIsTrue();
}
