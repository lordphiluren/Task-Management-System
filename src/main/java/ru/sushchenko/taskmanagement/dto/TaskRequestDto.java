package ru.sushchenko.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskRequestDto {
    @NotNull(message = "Title cannot be empty")
    @Size(min = 2, max = 100)
    private String title;
    @Column(name = "description")
    @Size(max = 512, message = "Maximum description size is 512")
    private String description;
    private UserDto executor;
    private Long statusId;
    private Long priorityId;
}
