package ru.sushchenko.taskmanagement.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sushchenko.taskmanagement.dto.TaskRequestDto;
import ru.sushchenko.taskmanagement.dto.TaskResponseDto;
import ru.sushchenko.taskmanagement.entity.Priority;
import ru.sushchenko.taskmanagement.entity.Status;
import ru.sushchenko.taskmanagement.entity.Task;

@Component
@RequiredArgsConstructor
public class TaskMapper{
    private final CustomModelMapper modelMapper;
    private final UserMapper userMapper;
    public TaskResponseDto toDto(Task task) {
        TaskResponseDto taskDto = modelMapper.map(task, TaskResponseDto.class);
        taskDto.setCreator(userMapper.toDto(task.getCreator()));
        taskDto.setExecutor(userMapper.toDto(task.getExecutor()));
        return taskDto;
    }

    public Task toEntity(TaskRequestDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        task.setStatus(Status.builder().id(taskDto.getStatusId()).build());
        task.setPriority(Priority.builder().id(taskDto.getPriorityId()).build());
        return task;
    }
}
