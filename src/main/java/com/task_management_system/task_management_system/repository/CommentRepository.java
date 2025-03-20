package com.task_management_system.task_management_system.repository;

import com.task_management_system.task_management_system.model.Comment;
import com.task_management_system.task_management_system.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
