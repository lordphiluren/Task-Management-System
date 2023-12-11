package ru.sushchenko.taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagement.entity.User;
import ru.sushchenko.taskmanagement.entity.enums.Role;
import ru.sushchenko.taskmanagement.repo.UserRepo;
import ru.sushchenko.taskmanagement.utils.exceptions.UserAlreadyExistsException;
import ru.sushchenko.taskmanagement.utils.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    @Transactional
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + "doesn't exist"));
    }
    @Transactional
    public void add(User user) {
        if (userRepo.findByEmail(user.getEmail()).isEmpty()) {
            user.setRole(Role.USER);
            userRepo.save(user);
        }
        else throw new UserAlreadyExistsException("Email is already taken");
    }
}
