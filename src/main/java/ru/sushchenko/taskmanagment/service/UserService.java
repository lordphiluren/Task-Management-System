package ru.sushchenko.taskmanagment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sushchenko.taskmanagment.entity.User;
import ru.sushchenko.taskmanagment.entity.enums.Role;
import ru.sushchenko.taskmanagment.repo.UserRepo;
import ru.sushchenko.taskmanagment.utils.exceptions.UserAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    public void add(User user) {
        if (userRepo.findByEmail(user.getEmail()).isEmpty()) {
            user.setRole(Role.ROLE_USER);
            userRepo.save(user);
        }
        else throw new UserAlreadyExistsException("Email is already taken");
    }
}
