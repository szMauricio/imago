package com.imago.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.imago.backend.exceptions.EmailAlreadyExistsException;
import com.imago.backend.exceptions.InvalidCredentialsException;
import com.imago.backend.exceptions.UserNotFoundException;
import com.imago.backend.models.User;
import com.imago.backend.models.enums.Role;
import com.imago.backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        if (user.getRole() == null) {
            user.setRole(Role.ROLE_CLIENT);
        }

        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User userDb = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userDetails.getFirstName() != null) {
            userDb.setFirstName(userDetails.getFirstName());
        }
        if (userDetails.getLastName() != null) {
            userDb.setLastName(userDetails.getLastName());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(userDb.getEmail())) {
            if (repository.existsByEmail(userDetails.getEmail())) {
                throw new EmailAlreadyExistsException(userDetails.getEmail());
            }
            userDb.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null) {
            userDb.setPassword(userDetails.getPassword());
        }
        if (userDetails.getRole() != null) {
            userDb.setRole(userDetails.getRole());
        }

        return repository.save(userDb);
    }

    @Override
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(user);
    }

    @Override
    public User validateLogin(String email, String password) {
        return repository.findByEmailAndPassword(email, password)
                .orElseThrow(InvalidCredentialsException::new);
    }
}
