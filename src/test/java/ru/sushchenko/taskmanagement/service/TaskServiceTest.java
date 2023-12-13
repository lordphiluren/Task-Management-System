package ru.sushchenko.taskmanagement.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sushchenko.taskmanagement.entity.Priority;
import ru.sushchenko.taskmanagement.entity.Status;
import ru.sushchenko.taskmanagement.entity.Task;
import ru.sushchenko.taskmanagement.repo.TaskRepo;
import ru.sushchenko.taskmanagement.utils.exceptions.TaskNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskServiceTest {
    @Mock
    private TaskRepo taskRepo;
    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldReturnAllTasksWithFilter() {
        Long statusId = 2L;
        Long priorityId = 3L;
        Integer offset = 0;
        Integer limit = 10;

        // given
        Task expectedTask = new Task();
        expectedTask.setStatus(Status.builder().id(statusId).build());
        expectedTask.setPriority(Priority.builder().id(priorityId).build());

        List<Task> expectedTasks = Collections.singletonList(new Task());

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Task> example = Example.of(expectedTask, matcher);

        // when
        if (offset != null && limit != null) {
            Page<Task> page = mock(Page.class);
            when(page.getContent()).thenReturn(expectedTasks);
            when(taskRepo.findAll(example, PageRequest.of(offset, limit))).thenReturn(page);
        } else {
            when(taskRepo.findAll(example)).thenReturn(expectedTasks);
        }

        List<Task> actualTasks = taskService.getAllTasks(statusId, priorityId, offset, limit);

        // then
        assertEquals(expectedTasks, actualTasks);

        if (offset != null && limit != null) {
            verify(taskRepo).findAll(example, PageRequest.of(offset, limit));
        } else {
            verify(taskRepo).findAll(example);
        }
    }

    @Test
    void shouldCreateTaskWithCurrentDateTime() {
        // given
        Task task = new Task();
        // when
        taskService.addTask(task);
        // then
        verify(taskRepo).save(task);
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void shouldReturnTaskWithSpecifiedId() {
        // given
        Long taskId = 1L;
        // when
        Optional<Task> optionalTask = Optional.empty();
        when(taskRepo.findById(taskId)).thenReturn(optionalTask);
        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
        verify(taskRepo).findById(taskId);
    }

    @Test
    void shouldDeleteTaskById() {
        // given
        Long taskId = 123L;
        // when
        when(taskRepo.findById(taskId)).thenReturn(Optional.of(new Task()));
        taskService.deleteTaskById(taskId);
        // then
        verify(taskRepo).deleteById(taskId);
    }

    @Test
    void shouldUpdateTask() {
        // given
        Task updatedTask = new Task();
        // when
        when(taskRepo.save(updatedTask)).thenReturn(updatedTask);
        taskService.updateTask(updatedTask);
        // then
        verify(taskRepo).save(updatedTask);
    }
}