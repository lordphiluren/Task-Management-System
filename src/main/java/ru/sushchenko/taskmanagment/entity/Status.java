package ru.sushchenko.taskmanagment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status")
public class Status {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    @NotNull(message = "Status name cannot be null")
    @Size(min = 2, max = 32, message = "Status name size should be between 2 and 32")
    private String name;
    @OneToMany(mappedBy = "status")
    private List<Task> tasks;
}
