package com.conestoga.projectdonut.dto;

import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.GameRating;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private Game game;

    private double rating;

    private int ratingNumber;

}
