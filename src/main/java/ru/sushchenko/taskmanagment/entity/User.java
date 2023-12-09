package ru.sushchenko.taskmanagment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sushchenko.taskmanagment.entity.enums.Role;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true)
    @NotNull(message = "Email cannot be empty")
    private String email;
    @Column(name = "password")
    @NotNull(message = "Password cannot be empty")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @OneToMany(mappedBy = "creator")
    private List<Task> createdTasks;
    @OneToMany(mappedBy = "executor")
    private List<Task> assignedTasks;
    @OneToMany(mappedBy = "creator")
    private List<Comment> comments;
}
