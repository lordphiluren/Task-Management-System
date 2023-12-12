package ru.sushchenko.taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagement.entity.Comment;
import ru.sushchenko.taskmanagement.repo.CommentRepo;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;
    @Transactional
    public void addComment(Comment comment) {
        comment.setCreatedAt(new Date());
        commentRepo.save(comment);
    }
}
