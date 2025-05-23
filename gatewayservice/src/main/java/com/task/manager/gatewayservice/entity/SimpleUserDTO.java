package com.task.manager.gatewayservice.entity;

public class SimpleUserDTO {
    private String username;
    private String password;
    
    public SimpleUserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}