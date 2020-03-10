package com.conestoga.projectdonut.controller;

import com.conestoga.projectdonut.dto.GenreGamesDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.Genre;
import com.conestoga.projectdonut.repository.GenreRepository;
import com.conestoga.projectdonut.service.GameService;
import com.conestoga.projectdonut.service.GenreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GenreService genreService;

    @PostMapping("/createGame")
    public Game createGame(@RequestParam("coverImage") MultipartFile coverImage, @RequestParam("gameData") String gameData,
            @RequestParam("gameGenre") String gameGenre)
            throws IOException {
        Genre genre = genreService.getByName(gameGenre);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        Game game = new ObjectMapper().readValue(gameData, Game.class);
        game.setGenres(genres);
        game.setCoverImage(gameService.compressBytes(coverImage.getBytes()));
        game.setFollowers(1);
        return gameService.saveGame(game);
    }

    @GetMapping("/get")
    public Game getGame(@RequestParam int id) {
        return gameService.getGame(id);
    }

    @GetMapping("/getAll")
    public List<GenreGamesDto> getGames() {
        List<GenreGamesDto> games = gameService.getGames();
        return games;
    }
}
