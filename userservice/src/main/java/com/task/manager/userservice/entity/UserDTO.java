package com.task.manager.userservice.entity;


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

    public UserDTO(long user_id, String username, String password, String role, LocalDateTime lastPasswordReset, String email, boolean acvalidated, String old_email) {
        this.user_id=user_id;
        this.username = username;
        this.password = password;
        this.role=role;
        this.lastPasswordReset=lastPasswordReset;
        this.email=email;
        this.acvalidated=acvalidated;
        this.old_email=old_email;
    }

        public boolean getAcvalidated(){
        return acvalidated;
    }
    public long getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getOldEmail(){
        return old_email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getLastPasswordReset(){
        return lastPasswordReset;
    }

    public String getRole(){
        return role;
    }

}
