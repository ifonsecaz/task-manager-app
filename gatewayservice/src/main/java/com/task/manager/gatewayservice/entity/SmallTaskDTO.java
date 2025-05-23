package com.task.manager.gatewayservice.entity;

import java.time.LocalDateTime;

public class SmallTaskDTO {
    private String title;
    private String description;
    private String priority;
    private LocalDateTime dueDate;

    public SmallTaskDTO() {
    }

    public SmallTaskDTO(String title, String description,String priority,
                LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

}