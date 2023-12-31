package ru.sushchenko.taskmanagement.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sushchenko.taskmanagement.dto.UserDto;
import ru.sushchenko.taskmanagement.entity.User;
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final CustomModelMapper modelMapper;
    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
