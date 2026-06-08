package com.university.service;

import com.university.model.User;
import com.university.repository.Database;

import java.util.Optional;

public class AuthService {
    private Database db;

    public AuthService() {
        this.db = Database.getInstance();
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = db.getUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.verifyPassword(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
