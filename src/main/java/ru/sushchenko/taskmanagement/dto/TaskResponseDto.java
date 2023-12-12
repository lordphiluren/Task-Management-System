package ru.sushchenko.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.sushchenko.taskmanagement.entity.Comment;
import ru.sushchenko.taskmanagement.entity.Priority;
import ru.sushchenko.taskmanagement.entity.Status;

import java.util.Date;
import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponseDto {
    private Long id;
    @NotNull(message = "Title cannot be empty")
    @Size(min = 2, max = 100)
    private String title;
    @Column(name = "description")
    @Size(max = 512, message = "Maximum description size is 512")
    private String description;
    private Date createdAt;
    private UserDto creator;
    private UserDto executor;
    private Status status;
    private Priority priority;
    private List<CommentResponseDto> comments;
}
