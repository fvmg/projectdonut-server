package com.conestoga.projectdonut.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String login;

    private String password;

    @OneToMany
    private List<Game> createdGames;

    @OneToMany
    private List<Game> recommendedGames;

    @OneToMany
    private List<Job> recommendedJobs;

    public void addCreatedGame(Game game) {
        this.createdGames.add(game);
    }
}
