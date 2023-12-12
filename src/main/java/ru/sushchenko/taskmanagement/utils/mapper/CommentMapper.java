package ru.sushchenko.taskmanagement.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sushchenko.taskmanagement.dto.CommentRequestDto;
import ru.sushchenko.taskmanagement.dto.CommentResponseDto;
import ru.sushchenko.taskmanagement.entity.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final CustomModelMapper modelMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    public CommentResponseDto toDto(Comment comment) {
        CommentResponseDto commentDto = modelMapper.map(comment, CommentResponseDto.class);
        commentDto.setCreator(userMapper.toDto(comment.getCreator()));
        return commentDto;
    }
    public Comment toEntity(CommentRequestDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}
