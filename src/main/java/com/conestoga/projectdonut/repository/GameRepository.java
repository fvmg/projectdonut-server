package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query(nativeQuery = true, value = "select count(*) from user_followed_games ufg where ufg.followed_games_id = :gameId")
    Integer getFollowers(@Param("gameId") int gameId);

}
