package com.task.manager.gatewayservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Tokenvalid {
    @Id
    private long user_id;
    private LocalDateTime last_password_reset;

    public Tokenvalid(){

    }

    public Tokenvalid(long user_id, LocalDateTime last_password_reset){
        this.user_id=user_id;
        this.last_password_reset=last_password_reset;
    }

    public long getUser_id(){
        return user_id;
    }

    public LocalDateTime getLast_password_reset(){
        return last_password_reset;
    }

    public void setLast_password_reset(LocalDateTime last_password_reset){
        this.last_password_reset=last_password_reset;
    }

}
