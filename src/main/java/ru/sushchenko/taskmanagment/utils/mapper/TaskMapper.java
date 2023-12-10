package ru.sushchenko.taskmanagment.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sushchenko.taskmanagment.dto.TaskRequestDto;
import ru.sushchenko.taskmanagment.dto.TaskResponseDto;
import ru.sushchenko.taskmanagment.entity.Priority;
import ru.sushchenko.taskmanagment.entity.Status;
import ru.sushchenko.taskmanagment.entity.Task;
import ru.sushchenko.taskmanagment.entity.User;

@Component
@RequiredArgsConstructor
public class TaskMapper{
    private final ModelMapper modelMapper;
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
