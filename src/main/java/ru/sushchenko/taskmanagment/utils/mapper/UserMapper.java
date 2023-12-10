package ru.sushchenko.taskmanagment.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sushchenko.taskmanagment.dto.UserDto;
import ru.sushchenko.taskmanagment.entity.User;
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
