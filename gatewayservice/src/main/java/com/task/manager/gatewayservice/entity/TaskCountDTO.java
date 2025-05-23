package com.task.manager.gatewayservice.entity;

public class TaskCountDTO {
    private long user_id;
    private int taskCount;

    public TaskCountDTO(){

    }

    public TaskCountDTO(long user_id, int taskCount){
        this.user_id=user_id;
        this.taskCount=taskCount;
    }

    public long getUser_id(){
        return user_id;
    }

    public int getTaskCount(){
        return taskCount;
    }
}
