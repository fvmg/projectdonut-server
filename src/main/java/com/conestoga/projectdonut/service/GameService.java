package com.conestoga.projectdonut.service;

import com.conestoga.projectdonut.dto.GameDto;
import com.conestoga.projectdonut.dto.GameForYouDto;
import com.conestoga.projectdonut.dto.GenreGamesDto;
import com.conestoga.projectdonut.dto.RateGameDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.GameImage;
import com.conestoga.projectdonut.entity.GameRating;
import com.conestoga.projectdonut.entity.Genre;
import com.conestoga.projectdonut.entity.Job;
import com.conestoga.projectdonut.entity.User;
import com.conestoga.projectdonut.repository.GameImageRepository;
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
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private GameImageRepository gameImageRepository;

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

    public byte[] getGameImg(int gameId) {
        Game game = gameRepository.getOne(gameId);
        return decompressBytes(game.getCoverImage());
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

    public void followGame(String userId, String gameId, String action) {
        User user = userRepository.getOne(Integer.parseInt(userId));
        Game game = gameRepository.getOne(Integer.parseInt(gameId));
        if (Boolean.parseBoolean(action)) {
            user.addFollowedGame(game);
        } else {
            user.deleteFollowedGame(game);
        }
        userRepository.save(user);
    }

    public Integer getFollowers(int gameId) {
        return gameRepository.getFollowers(gameId);
    }

    public void addImage(String gameId, MultipartFile image) throws IOException {
        Game game = gameRepository.getOne(Integer.parseInt(gameId));
        GameImage gameImage = new GameImage();
        gameImage.setImage(compressBytes(image.getBytes()));
        gameImage = gameImageRepository.save(gameImage);
        game.addImage(gameImage);
        gameRepository.save(game);
    }

    public List<GameImage> getImages(int gameId) {
        Game game = gameRepository.getOne(gameId);
        List<GameImage> gameImages = game.getImages();
        for (GameImage gameImage : gameImages) {
            gameImage.setImage(decompressBytes(gameImage.getImage()));
        }
        return gameImages;
    }

    public List<GameForYouDto> getForYouGames(int userId) {
        List<Integer> gameIds = gameRepository.getForYouGameIds(userId);
        List<Game> games = gameRepository.findAllById(gameIds);
        List<GameForYouDto> gameForYouDtos = new ArrayList<>();
        for (Game game : games) {
            GameForYouDto gameForYouDto = new GameForYouDto();
            gameForYouDto.setId(game.getId());
            gameForYouDto.setCoverImage(decompressBytes(game.getCoverImage()));
            gameForYouDto.setName(game.getName());
            gameForYouDto.setGenre(game.getBaseGenre() != null ? game.getBaseGenre().getName() : null);
            gameForYouDto.setDescription(game.getFullDescription());
            if (gameForYouDto.getDescription().length() > 250) {
                String description = gameForYouDto.getDescription().substring(0, 250) + "...";
                gameForYouDto.setDescription(description);
            }
            gameForYouDtos.add(gameForYouDto);
        }
        return gameForYouDtos;
    }

    public void recommendGame() {
        List<User> users = userRepository.findAll();
        List<Integer> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getId());
        }
        List<Game> games = gameRepository.findAllById(userIds);
        List<GameForYouDto> gameForYouDtos = new ArrayList<>();
        for (Game game : games) {
            GameForYouDto jobDto = new GameForYouDto();
            GameForYouDto gameForYouDto = new GameForYouDto();
            gameForYouDto.setId(game.getId());
            gameForYouDto.setCoverImage(decompressBytes(game.getCoverImage()));
            gameForYouDto.setName(game.getName());
            gameForYouDto.setGenre(game.getBaseGenre() != null ? game.getBaseGenre().getName() : null);
            gameForYouDto.setDescription(game.getFullDescription());
            if (gameForYouDto.getDescription().length() > 250) {
                String description = gameForYouDto.getDescription().substring(0, 250) + "...";
                gameForYouDto.setDescription(description);
            }
            gameForYouDtos.add(gameForYouDto);
        }
    }
}
