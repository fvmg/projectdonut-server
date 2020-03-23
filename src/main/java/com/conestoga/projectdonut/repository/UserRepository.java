package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.login = :login and u.password = :password")
    User login(@Param("login") String login, @Param("password") String password);

    User getByLogin(String login);
}
