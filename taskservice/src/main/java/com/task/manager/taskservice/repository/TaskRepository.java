package com.task.manager.taskservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.task.manager.taskservice.entity.Task;
import com.task.manager.taskservice.entity.TaskCountDTO;

import java.util.List;
import java.time.LocalDateTime;


public interface  TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByDueDateAndUserId(LocalDateTime dueDate, long userId);

    List<Task> findByPriorityAndUserId(String priority, long userId);

    List<Task> findByStatusAndUserId(String status, long userId);

    List<Task> findByUserId(long userId);

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findAll(Pageable pageable);

    @Query("SELECT p FROM Task p WHERE p.title LIKE %:title% AND p.userId = :userId")
    List<Task> findByTitleContaining(@Param("title") String title, @Param("userId") long userId);

    @Query("SELECT p FROM Task p WHERE p.dueDate>=:currDate AND p.dueDate<= :dueDate AND status!='Completed' AND p.userId = :userId")
    List<Task> findByNearDate(@Param("currDate") LocalDateTime currDate, @Param("dueDate") LocalDateTime dueDate, @Param("userId") long userId);

    //Usan los nombres en la entidad
    @Query("SELECT new com.task.manager.taskservice.entity.TaskCountDTO(t.userId, COUNT(t)) " +
           "FROM Task t " +
           "GROUP BY t.userId")
    List<TaskCountDTO> findUsersTaskCount();
}
