package com.conestoga.projectdonut.service;

import com.conestoga.projectdonut.dto.GenreGamesDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.Genre;
import com.conestoga.projectdonut.repository.GameRepository;
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

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Game getGame(int gameId) {
        return gameRepository.findById(gameId).orElse(null);
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
}
