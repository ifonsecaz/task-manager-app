package com.task.manager.gatewayservice.entity;

public class EmailChangeRequestDTO {
    public String email;
    public String password;

    public EmailChangeRequestDTO(String email,String password){
        this.email=email;
        this.password=password;
    }
}