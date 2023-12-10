package ru.sushchenko.taskmanagment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import lombok.Data;
import ru.sushchenko.taskmanagment.entity.enums.Role;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    private Long id;
    @Email(message = "Email should be valid")
    private String email;
    private Role role;
}
