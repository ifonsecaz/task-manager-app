package com.task.manager.taskservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long task_id;
    @Column(name="user_id")
    private long userId;
    private String title;
    private String description;
    private String status;
    private String priority;
    @Column(name="created_date")
    private LocalDateTime createdDate;
    @Column(name="due_date")
    private LocalDateTime dueDate;

    public Task() {
    }

    public Task(long user_id, String title, String description, 
                String status, String priority, LocalDateTime createdDate, 
                LocalDateTime dueDate) {
        this.userId = user_id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    public long getTask_id() {
        return task_id;
    }

    public long getuserId() {
        return userId;
    }

    public void setuserId(long user_id) {
        this.userId = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
