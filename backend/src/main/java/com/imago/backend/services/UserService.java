package com.imago.backend.services;

import java.util.List;

import com.imago.backend.models.User;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    User validateLogin(String email, String password);
}
