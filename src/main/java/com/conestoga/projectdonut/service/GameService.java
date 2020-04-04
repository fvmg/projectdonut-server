package com.conestoga.projectdonut.service;

import com.conestoga.projectdonut.dto.GameDto;
import com.conestoga.projectdonut.dto.GenreGamesDto;
import com.conestoga.projectdonut.dto.RateGameDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.GameRating;
import com.conestoga.projectdonut.entity.Genre;
import com.conestoga.projectdonut.entity.Job;
import com.conestoga.projectdonut.entity.User;
import com.conestoga.projectdonut.repository.GameRatingRepository;
import com.conestoga.projectdonut.repository.GameRepository;
import com.conestoga.projectdonut.repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GameRatingRepository gameRatingRepository;

    public Game updateGame(Game game, Genre baseGenre) {
        Game updateGame = gameRepository.getOne(game.getId());
        updateGame.setBriefDescription(game.getBriefDescription());
        updateGame.setFullDescription(game.getFullDescription());
        updateGame.setName(game.getName());
        updateGame.setReleaseDate(game.getReleaseDate());
        updateGame.setVersion(game.getVersion());
        updateGame.setDownloadLink(game.getDownloadLink());
        if (game.getCoverImage() != null) {
            updateGame.setCoverImage(game.getCoverImage());
        }
        return gameRepository.save(updateGame);
    }

    public Game saveGame(Game game, String userId) {
        game = gameRepository.save(game);
        userService.saveGame(game, userId);
        return game;
    }

    public void saveJob(Job job, String gameId) {
        Game game = gameRepository.getOne(Integer.parseInt(gameId));
        game.addJob(job);
        gameRepository.save(game);
    }

    public GameDto getGame(int gameId) {
        GameDto gameDto = new GameDto();
        Game game = gameRepository.findById(gameId).orElse(null);
        List<GameRating> gameRatings = gameRatingRepository.findAllByGame(game);
        if (game != null) {
            game.setCoverImage(this.decompressBytes(game.getCoverImage()));
        }
        gameDto.setGame(game);
        gameDto.setRating(calculateRating(gameRatings));
        gameDto.setRatingNumber(gameRatings.size());
        return gameDto;
    }

    private double calculateRating(List<GameRating> gameRatings) {
        double sum = 0D;
        for (GameRating gameRating : gameRatings) {
            sum += gameRating.getRating();
        }
        return !gameRatings.isEmpty() ? sum / gameRatings.size() : 0D;
    }

    public List<GenreGamesDto> getGames() {
        List<Game> games = gameRepository.findAll();
        List<GenreGamesDto> dtos = new ArrayList<>();
        for (Game game: games) {
            game.setCoverImage(this.decompressBytes(game.getCoverImage()));
            containsGenre(dtos, game);
        }
        return dtos;
    }

    private void containsGenre(List<GenreGamesDto> genreGamesDtos, Game game) {
        for (GenreGamesDto dto : genreGamesDtos) {
            for (Genre genre : game.getGenres()) {
                if (dto.getGenre().equals(genre.getName())) {
                    dto.addGame(game);
                    return;
                }
            }
        }
        for (Genre genre : game.getGenres()) {
            List<Game> games = new ArrayList<>();
            games.add(game);
            GenreGamesDto genreGamesDto = new GenreGamesDto(genre.getName(), games);
            genreGamesDtos.add(genreGamesDto);
        }
    }

    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    public void rateGame(RateGameDto rateGameDto) {
        User user = userRepository.getOne(rateGameDto.getUserId());
        Game game = gameRepository.getOne(rateGameDto.getGameId());
        GameRating gameRating = new GameRating();
        gameRating.setComment(rateGameDto.getComments());
        gameRating.setRating(rateGameDto.getRating());
        gameRating.setUser(user);
        gameRating.setGame(game);
        gameRatingRepository.save(gameRating);
    }

    public boolean containsJob(String gameId, Job job) {
        Game game = gameRepository.getOne(Integer.parseInt(gameId));
        for (Job gameJob : game.getJobs()) {
            if (gameJob.equals(job)) {
                return true;
            }
        }
        return false;
    }
}
