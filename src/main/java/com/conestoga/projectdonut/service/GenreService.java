package com.conestoga.projectdonut.service;

import com.conestoga.projectdonut.dto.GenreGamesDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.Genre;
import com.conestoga.projectdonut.repository.GameRepository;
import com.conestoga.projectdonut.repository.GenreRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllBase() {
        return genreRepository.findAllByBaseGenreIsTrue();
    }

    public Genre getByName(String name) {
        return genreRepository.findByNameEquals(name);
    }
}
