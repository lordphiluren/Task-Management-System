package ru.sushchenko.taskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "priority")
public class Priority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    @NotNull(message = "Priority name cannot be null")
    @Size(min = 2, max = 32, message = "Priority name size should be between 2 and 32")
    private String name;
    @OneToMany(mappedBy = "priority", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasks;
}
