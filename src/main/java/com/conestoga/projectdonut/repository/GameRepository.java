package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {

}
