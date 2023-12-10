package ru.sushchenko.taskmanagment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagment.entity.User;
import ru.sushchenko.taskmanagment.entity.enums.Role;
import ru.sushchenko.taskmanagment.repo.UserRepo;
import ru.sushchenko.taskmanagment.utils.exceptions.UserAlreadyExistsException;
import ru.sushchenko.taskmanagment.utils.exceptions.UserNotFoundException;

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
