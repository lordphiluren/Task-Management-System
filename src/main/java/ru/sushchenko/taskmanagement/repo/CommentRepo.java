package ru.sushchenko.taskmanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sushchenko.taskmanagement.entity.Comment;
@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
}
