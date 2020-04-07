package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query(nativeQuery = true, value = "select count(*) from user_followed_games ufg where ufg.followed_games_id = :gameId")
    Integer getFollowers(@Param("gameId") int gameId);

    @Query(nativeQuery = true, value = "select urg.recommended_games_id from user_recommended_games urg where urg.user_id = :userId")
    List<Integer> getForYouGameIds(@Param("userId") int userId);

}
