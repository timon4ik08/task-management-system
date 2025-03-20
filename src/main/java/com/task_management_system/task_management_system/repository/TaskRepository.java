package com.task_management_system.task_management_system.repository;

import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAssigneeId(Long id, Pageable pageable);
//    Page<Task> findByAuthorOrAssignee(User author, User assignee, Pageable pageable);
//
//    Page<Task> findByAssigneeId(Long userId, Pageable pageable);
}

