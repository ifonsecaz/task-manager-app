package com.task.manager.gatewayservice.entity;

import java.time.LocalDateTime;

public class UserDTO {
    private long user_id;
    private String username;
    private String password;
    private String role;
    private LocalDateTime lastPasswordReset;
    private String email;
    private boolean acvalidated;
    private String old_email;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email=email;
        lastPasswordReset=LocalDateTime.now();
    }

    public boolean getAcvalidated(){
        return acvalidated;
    }

    public void setAcvalidated(boolean acvalidated){
        this.acvalidated=acvalidated;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getOldEmail(){
        return old_email;
    }

    public void setOldEmail(String old_email){
        this.old_email=old_email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getLastPasswordReset(){
        return lastPasswordReset;
    }

    public void setLastPasswordReset(){
        lastPasswordReset=LocalDateTime.now();
    }

    public void setPassword(String password) {
        this.password = password;
        lastPasswordReset=LocalDateTime.now();
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role) {
        this.role=role;
    }

}
