package com.conestoga.projectdonut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateGameDto {

    private int userId;

    private int rating;

    private String comments;

    private int gameId;
}
