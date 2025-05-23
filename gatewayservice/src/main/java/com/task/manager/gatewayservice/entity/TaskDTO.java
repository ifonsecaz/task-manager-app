package com.task.manager.gatewayservice.entity;

import java.time.LocalDateTime;

public class TaskDTO {
    private long task_id;
    private long userId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;

    public TaskDTO() {
    }

    public TaskDTO(long user_id, String title, String description, 
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
