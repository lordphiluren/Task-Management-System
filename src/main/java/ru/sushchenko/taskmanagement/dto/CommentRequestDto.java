package ru.sushchenko.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequestDto {
    @Size(max=512, message = "Comment message length should not be greater than 512 characters")
    @NotNull(message = "Comment message cannot be empty")
    private String message;
}
