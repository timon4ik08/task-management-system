package com.task_management_system.task_management_system.repository;

import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.filter.TaskFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t FROM Task t 
            WHERE 
            (:#{#filter.title} IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :#{#filter.title}, '%'))) AND
            (:#{#filter.status} IS NULL OR t.status = :#{#filter.status}) AND
            (:#{#filter.priority} IS NULL OR t.priority = :#{#filter.priority}) AND
            (:#{#filter.authorId} IS NULL OR t.author.id = :#{#filter.authorId}) AND
            (:#{#filter.authorEmail} IS NULL OR t.author.email = :#{#filter.authorEmail}) AND
            (:#{#filter.assigneeId} IS NULL OR t.assignee.id = :#{#filter.assigneeId}) AND
            (:#{#filter.assigneeEmail} IS NULL OR t.assignee.email = :#{#filter.assigneeEmail}) AND
            (:#{#filter.createdAt} IS NULL OR t.createdAt >= :#{#filter.createdAt})
            """)
    Page<Task> findAllWithFilters(
            @Param("filter") TaskFilter filter,
            Pageable pageable);

    @Query("""
            SELECT t FROM Task t
            JOIN t.assignee a
            LEFT JOIN t.author au
            WHERE a.id = :assigneeId
            AND (:#{#filter.title} IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :#{#filter.title}, '%')))
            AND (:#{#filter.status} IS NULL OR t.status = :#{#filter.status})
            AND (:#{#filter.priority} IS NULL OR t.priority = :#{#filter.priority})
            AND (:#{#filter.authorId} IS NULL OR au.id = :#{#filter.authorId})
            AND (:#{#filter.authorEmail} IS NULL OR au.email = :#{#filter.authorEmail})
            AND (:#{#filter.createdAt} IS NULL OR t.createdAt >= :#{#filter.createdAt})
            """)
    Page<Task> findByAssigneeId(
            @Param("assigneeId") Long assigneeId,
            @Param("filter") TaskFilter filter,
            Pageable pageable);
}


