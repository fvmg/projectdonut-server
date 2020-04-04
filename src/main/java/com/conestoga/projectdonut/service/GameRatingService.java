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
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRatingService {

    @Autowired
    private GameRatingRepository gameRatingRepository;

    @Autowired
    private GameRepository gameRepository;

    public List<RateGameDto> getComments(int gameId) {
        Game game = gameRepository.getOne(gameId);
        List<GameRating> gameRatings = gameRatingRepository.findAllByGame(game);
        List<RateGameDto> rateGameDtos = new ArrayList<>();
        for (GameRating gameRating : gameRatings) {
            RateGameDto rateGameDto = new RateGameDto();
            rateGameDto.setComments(gameRating.getComment());
            rateGameDto.setRating(gameRating.getRating());
            rateGameDtos.add(rateGameDto);
        }
        return rateGameDtos;
    }
}
