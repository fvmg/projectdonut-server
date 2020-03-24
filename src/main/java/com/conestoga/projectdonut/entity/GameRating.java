package com.conestoga.projectdonut.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameRating {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Game game;

    private double rating;

    private String comment;

    @OneToOne
    private User user;
}
