package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.GameRating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRatingRepository extends JpaRepository<GameRating, Integer> {

    List<GameRating> findAllByGame(Game game);
}
