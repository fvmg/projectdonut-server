package com.conestoga.projectdonut.service;


import com.conestoga.projectdonut.dto.UserLoginDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.User;
import com.conestoga.projectdonut.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User login(UserLoginDto userLoginDto) {
        User user = userRepository.getByLogin(userLoginDto.getLogin());
        if (user != null && passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            return user;
        }
        else {
            return null;
        }
    }

    public User checkToken(UserLoginDto userLoginDto) {
        User user = userRepository.getByLogin(userLoginDto.getLogin());
        if (user != null && user.getPassword().equals(userLoginDto.getPassword())) {
            return user;
        }
        else {
            return null;
        }
    }

    public void saveGame(Game game, String userId) {
        User user = userRepository.getOne(Integer.parseInt(userId));
        user.addCreatedGame(game);
        userRepository.save(user);
    }

    public User checkOwner(int userId, int gameId) {
        User user = userRepository.getOne(userId);
        for (Game game : user.getCreatedGames()) {
            if (game.getId() == gameId) {
                return user;
            }
        }
        return null;
    }
}
