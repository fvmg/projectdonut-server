package com.conestoga.projectdonut.dto;

import com.conestoga.projectdonut.entity.Game;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreGamesDto {

    private String genre;

    private List<Game> games = new ArrayList<>();

    public void addGame(Game game) {
        games.add(game);
    }
}
