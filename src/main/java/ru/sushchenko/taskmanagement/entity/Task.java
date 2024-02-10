package ru.sushchenko.taskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="task")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    @NotNull(message = "Title cannot be empty")
    @Size(min = 2, max = 100)
    private String title;
    @Column(name = "description")
    @Size(max = 512, message = "Maximum description size is 512")
    private String description;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, referencedColumnName = "id")
    private User creator;
    @ManyToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private User executor;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    private Priority priority;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Comment> comments;
}
