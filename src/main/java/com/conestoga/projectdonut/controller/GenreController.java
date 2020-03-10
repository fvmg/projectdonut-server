package com.conestoga.projectdonut.controller;

import com.conestoga.projectdonut.entity.Genre;
import com.conestoga.projectdonut.repository.GenreRepository;
import com.conestoga.projectdonut.service.GenreService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping("/getAllBase")
    public List<Genre> getAllBase() {
        return genreService.getAllBase();
    }
}
