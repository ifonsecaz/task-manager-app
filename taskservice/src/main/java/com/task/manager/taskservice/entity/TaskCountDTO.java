package com.task.manager.taskservice.entity;

public class TaskCountDTO {
    private long user_id;
    private long taskCount;

    public TaskCountDTO(){

    }

    public TaskCountDTO(long user_id, long taskCount){
        this.user_id=user_id;
        this.taskCount=taskCount;
    }

    public long getUser_id(){
        return user_id;
    }

    public long getTaskCount(){
        return taskCount;
    }
}
